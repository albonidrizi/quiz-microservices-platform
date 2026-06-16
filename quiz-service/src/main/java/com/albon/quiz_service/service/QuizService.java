package com.albon.quiz_service.service;

import com.albon.quiz_service.dao.QuizDao;
import com.albon.quiz_service.exception.InvalidRequestException;
import com.albon.quiz_service.exception.ResourceNotFoundException;
import com.albon.quiz_service.exception.ServiceUnavailableException;
import com.albon.quiz_service.feign.QuizInterface;
import com.albon.quiz_service.model.QuestionWrapper;
import com.albon.quiz_service.model.Quiz;
import com.albon.quiz_service.model.Response;
import feign.FeignException;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@Service
public class QuizService {

    private final QuizDao quizDao;
    private final QuizInterface quizInterface;

    public QuizService(QuizDao quizDao, QuizInterface quizInterface) {
        this.quizDao = quizDao;
        this.quizInterface = quizInterface;
    }

    public Quiz createQuiz(String category, int numQ, String title) {
        List<Integer> questions = callQuestionService(
                () -> quizInterface.getQuestionsForQuiz(category, numQ),
                "Question service returned no question ids");
        if (questions.isEmpty()) {
            throw new InvalidRequestException("No questions are available for category " + category);
        }
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        return quizDao.save(quiz);
    }

    public List<QuestionWrapper> getQuizQuestions(Integer id) {
        Quiz quiz = findQuiz(id);
        return callQuestionService(
                () -> quizInterface.getQuestionsFromId(quiz.getQuestionIds()),
                "Question service returned no questions");
    }

    public int calculateResult(Integer id, List<Response> responses) {
        Quiz quiz = findQuiz(id);
        validateResponsesBelongToQuiz(quiz, responses);
        return callQuestionService(
                () -> quizInterface.getScore(responses),
                "Question service returned no score");
    }

    private Quiz findQuiz(Integer id) {
        return quizDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz " + id + " was not found"));
    }

    private void validateResponsesBelongToQuiz(Quiz quiz, List<Response> responses) {
        if (responses == null || responses.isEmpty()) {
            throw new InvalidRequestException("At least one response is required");
        }

        Set<Integer> responseIds = new HashSet<>();
        for (Response response : responses) {
            if (response == null || response.getId() == null || response.getResponse() == null) {
                throw new InvalidRequestException("Every response must include a question id and answer");
            }
            if (!responseIds.add(response.getId())) {
                throw new InvalidRequestException("Duplicate response for question " + response.getId());
            }
        }

        Set<Integer> expectedIds = new HashSet<>(quiz.getQuestionIds());
        if (!expectedIds.equals(responseIds)) {
            throw new InvalidRequestException("Responses must contain exactly one answer for every quiz question");
        }
    }

    private <T> T callQuestionService(Supplier<ResponseEntity<T>> call, String emptyBodyMessage) {
        try {
            return requireBody(call.get(), emptyBodyMessage);
        } catch (FeignException ex) {
            throw mapFeignException(ex);
        } catch (NoFallbackAvailableException ex) {
            if (ex.getCause() instanceof FeignException feignException) {
                throw mapFeignException(feignException);
            }
            throw new ServiceUnavailableException("Question service is currently unavailable", ex);
        }
    }

    private RuntimeException mapFeignException(FeignException ex) {
        if (ex.status() == 400) {
            return new InvalidRequestException("Question service rejected the request");
        }
        if (ex.status() == 404) {
            return new ResourceNotFoundException("Question service resource was not found");
        }
        return new ServiceUnavailableException("Question service is currently unavailable", ex);
    }

    private <T> T requireBody(ResponseEntity<T> response, String message) {
        if (response.getStatusCode().value() == 404) {
            throw new ResourceNotFoundException(message);
        }
        if (response.getStatusCode().is4xxClientError()) {
            throw new InvalidRequestException(message);
        }
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new ServiceUnavailableException(message);
        }
        return response.getBody();
    }
}
