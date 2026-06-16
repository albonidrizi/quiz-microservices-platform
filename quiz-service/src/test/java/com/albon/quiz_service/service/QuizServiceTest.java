package com.albon.quiz_service.service;

import com.albon.quiz_service.dao.QuizDao;
import com.albon.quiz_service.exception.InvalidRequestException;
import com.albon.quiz_service.exception.ServiceUnavailableException;
import com.albon.quiz_service.feign.QuizInterface;
import com.albon.quiz_service.model.Quiz;
import com.albon.quiz_service.model.Response;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.client.circuitbreaker.NoFallbackAvailableException;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class QuizServiceTest {

    @Mock
    QuizDao quizDao;

    @Mock
    QuizInterface quizInterface;

    @InjectMocks
    QuizService quizService;

    @Test
    public void createQuizTest() {
        // Mocking the Feign Client call
        List<Integer> questions = new ArrayList<>();
        questions.add(1);
        questions.add(2);
        when(quizInterface.getQuestionsForQuiz(anyString(), anyInt()))
                .thenReturn(ResponseEntity.ok(questions));

        // Mock the save method
        Quiz mockQuiz = new Quiz();
        mockQuiz.setId(1);
        mockQuiz.setTitle("Java Basics");
        mockQuiz.setQuestionIds(questions);
        when(quizDao.save(any(Quiz.class))).thenReturn(mockQuiz);

        // Calling the method under test
        Quiz response = quizService.createQuiz("Java", 5, "Java Basics");

        // Verifying checks
        assertEquals("Java Basics", response.getTitle());
        verify(quizDao).save(any(Quiz.class)); // Verify save was called
    }

    @Test
    void createQuizRejectsQuestionServiceClientErrors() {
        when(quizInterface.getQuestionsForQuiz("Java", 5))
                .thenReturn(ResponseEntity.badRequest().build());

        assertThrows(InvalidRequestException.class, () -> quizService.createQuiz("Java", 5, "Java Basics"));
    }

    @Test
    void createQuizRejectsCircuitBreakerWrappedClientErrors() {
        FeignException feignException = feignException(400);
        when(quizInterface.getQuestionsForQuiz("Java", 5))
                .thenThrow(new NoFallbackAvailableException("No fallback available", feignException));

        assertThrows(InvalidRequestException.class, () -> quizService.createQuiz("Java", 5, "Java Basics"));
    }

    @Test
    void createQuizTreatsQuestionServiceFailuresAsUnavailable() {
        when(quizInterface.getQuestionsForQuiz("Java", 5))
                .thenReturn(ResponseEntity.status(503).build());

        assertThrows(ServiceUnavailableException.class, () -> quizService.createQuiz("Java", 5, "Java Basics"));
    }

    @Test
    void calculateResultRejectsResponsesFromAnotherQuiz() {
        Quiz quiz = new Quiz(1, "Java Basics", List.of(1, 2));
        when(quizDao.findById(1)).thenReturn(java.util.Optional.of(quiz));

        List<Response> responses = List.of(new Response(1, "answer"), new Response(3, "answer"));

        assertThrows(InvalidRequestException.class, () -> quizService.calculateResult(1, responses));
    }

    @Test
    void calculateResultRejectsDuplicateResponses() {
        Quiz quiz = new Quiz(1, "Java Basics", List.of(1, 2));
        when(quizDao.findById(1)).thenReturn(java.util.Optional.of(quiz));

        List<Response> responses = List.of(new Response(1, "answer"), new Response(1, "answer"));

        assertThrows(InvalidRequestException.class, () -> quizService.calculateResult(1, responses));
    }

    @Test
    void calculateResultScoresOnlyValidatedQuizQuestions() {
        Quiz quiz = new Quiz(1, "Java Basics", List.of(1, 2));
        List<Response> responses = List.of(new Response(1, "answer"), new Response(2, "answer"));
        when(quizDao.findById(1)).thenReturn(java.util.Optional.of(quiz));
        when(quizInterface.getScore(responses)).thenReturn(ResponseEntity.ok(2));

        assertEquals(2, quizService.calculateResult(1, responses));
    }

    private FeignException feignException(int status) {
        Request request = Request.create(Request.HttpMethod.GET, "/question/generate", Map.of(),
                (byte[]) null, StandardCharsets.UTF_8);
        feign.Response response = feign.Response.builder()
                .status(status)
                .reason("Error")
                .request(request)
                .build();
        return FeignException.errorStatus("Question service call", response);
    }
}
