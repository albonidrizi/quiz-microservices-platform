package com.albon.questionservice.service;

import com.albon.questionservice.dao.QuestionDao;
import com.albon.questionservice.exception.InvalidRequestException;
import com.albon.questionservice.exception.ResourceNotFoundException;
import com.albon.questionservice.model.CreateQuestionRequest;
import com.albon.questionservice.model.Question;
import com.albon.questionservice.model.QuestionWrapper;
import com.albon.questionservice.model.Response;
import org.springframework.stereotype.Service;

import com.albon.questionservice.model.QuestionDTO;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class QuestionService {
    private final QuestionDao questionDao;

    public QuestionService(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    public List<QuestionDTO> getAllQuestions() {
        return questionDao.findAll().stream().map(this::toDto).toList();
    }

    public List<QuestionDTO> getQuestionsByCategory(String category) {
        return questionDao.findByCategory(category).stream().map(this::toDto).toList();
    }

    public QuestionDTO addQuestion(CreateQuestionRequest request) {
        List<String> options = List.of(request.option1(), request.option2(), request.option3(), request.option4());
        if (!options.contains(request.rightAnswer())) {
            throw new InvalidRequestException("rightAnswer must match one of the provided options");
        }
        Question question = new Question(null, request.questionTitle(), request.option1(), request.option2(),
                request.option3(), request.option4(), request.rightAnswer(), request.difficultylevel(),
                request.category());
        return toDto(questionDao.save(question));
    }

    public List<Integer> getQuestionsForQuiz(String categoryName, Integer numQuestions) {
        List<Integer> questions = questionDao.findRandomQuestionsByCategory(categoryName, numQuestions);
        if (questions.isEmpty()) {
            throw new ResourceNotFoundException("No questions found for category " + categoryName);
        }
        if (questions.size() < numQuestions) {
            throw new InvalidRequestException("Only " + questions.size()
                    + " questions are available for category " + categoryName);
        }
        return questions;
    }

    public List<QuestionWrapper> getQuestionsFromId(List<Integer> questionIds) {
        if (questionIds == null || questionIds.isEmpty()) {
            throw new InvalidRequestException("At least one question id is required");
        }
        return questionIds.stream().map(this::findQuestion).map(this::toWrapper).toList();
    }

    public int getScore(List<Response> responses) {
        if (responses == null || responses.isEmpty()) {
            throw new InvalidRequestException("At least one response is required");
        }
        int right = 0;
        Set<Integer> seenQuestionIds = new HashSet<>();

        for (Response response : responses) {
            if (response == null || response.getId() == null || response.getResponse() == null) {
                throw new InvalidRequestException("Every response must include a question id and answer");
            }
            if (!seenQuestionIds.add(response.getId())) {
                throw new InvalidRequestException("Duplicate response for question " + response.getId());
            }
            Question question = findQuestion(response.getId());
            if (response.getResponse().equals(question.getRightAnswer()))
                right++;
        }
        return right;
    }

    private Question findQuestion(Integer id) {
        return questionDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question " + id + " was not found"));
    }

    private QuestionDTO toDto(Question question) {
        return new QuestionDTO(question.getId(), question.getQuestionTitle(), question.getOption1(),
                question.getOption2(), question.getOption3(), question.getOption4(), question.getDifficultylevel(),
                question.getCategory());
    }

    private QuestionWrapper toWrapper(Question question) {
        return new QuestionWrapper(question.getId(), question.getQuestionTitle(), question.getOption1(),
                question.getOption2(), question.getOption3(), question.getOption4());
    }
}
