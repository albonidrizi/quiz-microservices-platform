package com.albon.quiz_service.service;

import com.albon.quiz_service.dao.QuizDao;
import com.albon.quiz_service.feign.QuizInterface;
import com.albon.quiz_service.model.QuestionWrapper;
import com.albon.quiz_service.model.Quiz;
import com.albon.quiz_service.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuizInterface quizInterface;

    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = "question-service", fallbackMethod = "createQuizFallback")
    public ResponseEntity<Quiz> createQuiz(String category, int numQ, String title) {

        List<Integer> questions = quizInterface.getQuestionsForQuiz(category, numQ).getBody();
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        Quiz savedQuiz = quizDao.save(quiz);

        return new ResponseEntity<>(savedQuiz, HttpStatus.CREATED);

    }

    public ResponseEntity<Quiz> createQuizFallback(String category, int numQ, String title, Throwable t) {
        return new ResponseEntity<>(new Quiz(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Quiz quiz = quizDao.findById(id).get();
        List<Integer> questionIds = quiz.getQuestionIds();
        ResponseEntity<List<QuestionWrapper>> questions = quizInterface.getQuestionsFromId(questionIds);
        return questions;

    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        ResponseEntity<Integer> score = quizInterface.getScore(responses);
        return score;
    }
}
