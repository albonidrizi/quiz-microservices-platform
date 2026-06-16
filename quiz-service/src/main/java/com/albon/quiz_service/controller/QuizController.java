package com.albon.quiz_service.controller;

import com.albon.quiz_service.model.QuestionWrapper;
import com.albon.quiz_service.model.QuizDto;
import com.albon.quiz_service.model.Response;
import com.albon.quiz_service.service.QuizService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("quiz")
@Validated
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("ready")
    public Map<String, String> ready() {
        return Map.of("status", "UP");
    }

    @PostMapping("create")
    public ResponseEntity<com.albon.quiz_service.model.Quiz> createQuiz(@Valid @RequestBody QuizDto quizDto) {
        return ResponseEntity.status(201)
                .body(quizService.createQuiz(quizDto.getCategoryName(), quizDto.getNumQuestions(), quizDto.getTitle()));
    }

    @GetMapping("get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable @Min(1) Integer id) {
        return ResponseEntity.ok(quizService.getQuizQuestions(id));
    }

    @PostMapping("submit/{id}")
    public ResponseEntity<Integer> submitQuiz(@PathVariable @Min(1) Integer id, @RequestBody List<Response> responses) {
        return ResponseEntity.ok(quizService.calculateResult(id, responses));
    }

}
