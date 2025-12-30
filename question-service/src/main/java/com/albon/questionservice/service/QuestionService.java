package com.albon.questionservice.service;

import com.albon.questionservice.dao.QuestionDao;
import com.albon.questionservice.model.Question;
import com.albon.questionservice.model.QuestionWrapper;
import com.albon.questionservice.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.albon.questionservice.model.QuestionDTO;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<List<QuestionDTO>> getAllQuestions() {
        try {
            List<Question> questions = questionDao.findAll();
            List<QuestionDTO> questionDTOS = new ArrayList<>();
            for (Question q : questions) {
                QuestionDTO questionDTO = new QuestionDTO(q.getId(), q.getQuestionTitle(), q.getOption1(),
                        q.getOption2(), q.getOption3(), q.getOption4(), q.getDifficultylevel(), q.getCategory());
                questionDTOS.add(questionDTO);
            }
            return new ResponseEntity<>(questionDTOS, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<QuestionDTO>> getQuestionsByCategory(String category) {
        try {
            List<Question> questions = questionDao.findByCategory(category);
            List<QuestionDTO> questionDTOS = new ArrayList<>();
            for (Question q : questions) {
                QuestionDTO questionDTO = new QuestionDTO(q.getId(), q.getQuestionTitle(), q.getOption1(),
                        q.getOption2(), q.getOption3(), q.getOption4(), q.getDifficultylevel(), q.getCategory());
                questionDTOS.add(questionDTO);
            }
            return new ResponseEntity<>(questionDTOS, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<String> addQuestion(Question question) {
        questionDao.save(question);
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String categoryName, Integer numQuestions) {
        List<Integer> questions = questionDao.findRandomQuestionsByCategory(categoryName, numQuestions);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {
        List<QuestionWrapper> wrappers = new ArrayList<>();
        List<Question> questions = new ArrayList<>();

        for (Integer id : questionIds) {
            questions.add(questionDao.findById(id).get());
        }

        for (Question question : questions) {
            QuestionWrapper wrapper = new QuestionWrapper();
            wrapper.setId(question.getId());
            wrapper.setQuestionTitle(question.getQuestionTitle());
            wrapper.setOption1(question.getOption1());
            wrapper.setOption2(question.getOption2());
            wrapper.setOption3(question.getOption3());
            wrapper.setOption4(question.getOption4());
            wrappers.add(wrapper);
        }

        return new ResponseEntity<>(wrappers, HttpStatus.OK);
    }

    public ResponseEntity<Integer> getScore(List<Response> responses) {

        int right = 0;

        for (Response response : responses) {
            Question question = questionDao.findById(response.getId()).get();
            if (response.getResponse().equals(question.getRightAnswer()))
                right++;
        }
        return new ResponseEntity<>(right, HttpStatus.OK);
    }

}
