package com.albon.questionservice.service;

import com.albon.questionservice.dao.QuestionDao;
import com.albon.questionservice.exception.InvalidRequestException;
import com.albon.questionservice.exception.ResourceNotFoundException;
import com.albon.questionservice.model.Question;
import com.albon.questionservice.model.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    QuestionDao questionDao;

    @InjectMocks
    QuestionService questionService;

    @Test
    void getScoreRejectsDuplicateQuestionIds() {
        List<Response> responses = List.of(new Response(1, "Map"), new Response(1, "Map"));
        when(questionDao.findById(1)).thenReturn(Optional.of(question(1, "Map")));

        assertThrows(InvalidRequestException.class, () -> questionService.getScore(responses));
    }

    @Test
    void getScoreRejectsUnknownQuestion() {
        when(questionDao.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> questionService.getScore(List.of(new Response(99, "answer"))));
    }

    @Test
    void getScoreCountsCorrectAnswers() {
        when(questionDao.findById(1)).thenReturn(Optional.of(question(1, "Map")));
        when(questionDao.findById(2)).thenReturn(Optional.of(question(2, "Object")));

        int score = questionService.getScore(List.of(new Response(1, "Map"), new Response(2, "Wrong")));

        assertEquals(1, score);
    }

    @Test
    void getQuestionsForQuizRejectsInsufficientAvailableQuestions() {
        when(questionDao.findRandomQuestionsByCategory("Java", 5)).thenReturn(List.of(1, 2));

        assertThrows(InvalidRequestException.class, () -> questionService.getQuestionsForQuiz("Java", 5));
    }

    private Question question(int id, String rightAnswer) {
        return new Question(id, "Question", "A", "B", "C", "D", rightAnswer, "Easy", "Java");
    }
}
