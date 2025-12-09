package com.albon.questionservice;

import com.albon.questionservice.dao.QuestionDao;
import com.albon.questionservice.model.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class QuestionServiceIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @Autowired
    QuestionDao questionDao;

    @Test
    void testSaveAndRetrieveQuestion() {
        Question question = new Question();
        question.setQuestionTitle("What is Java?");
        question.setOption1("OS");
        question.setOption2("Programming Language");
        question.setOption3("Food");
        question.setOption4("Car");
        question.setRightAnswer("Programming Language");
        question.setDifficultylevel("Easy");
        question.setCategory("Java");

        questionDao.save(question);

        List<Question> questions = questionDao.findAll();
        assertEquals(1, questions.size());
        assertEquals("What is Java?", questions.get(0).getQuestionTitle());
    }
}
