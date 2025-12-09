package com.albon.quiz_service.service;

import com.albon.quiz_service.dao.QuizDao;
import com.albon.quiz_service.feign.QuizInterface;
import com.albon.quiz_service.model.Quiz;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
                .thenReturn(new ResponseEntity<>(questions, HttpStatus.OK));

        // Calling the method under test
        ResponseEntity<Quiz> response = quizService.createQuiz("Java", 5, "Java Basics");

        // Verifying checks
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Java Basics", response.getBody().getTitle());
        verify(quizDao).save(any(Quiz.class)); // Verify save was called
    }
}
