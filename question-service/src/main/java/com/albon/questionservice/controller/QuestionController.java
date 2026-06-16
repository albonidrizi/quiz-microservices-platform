package com.albon.questionservice.controller;

import com.albon.questionservice.model.CreateQuestionRequest;
import com.albon.questionservice.model.QuestionWrapper;
import com.albon.questionservice.model.Response;
import com.albon.questionservice.model.QuestionDTO;
import com.albon.questionservice.service.QuestionService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("question")
@Validated
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("allQuestions")
    public ResponseEntity<List<QuestionDTO>> getAllQuestions() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    @GetMapping("category/{category}")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByCategory(@PathVariable @NotBlank String category) {
        return ResponseEntity.ok(questionService.getQuestionsByCategory(category));
    }

    @PostMapping("add")
    public ResponseEntity<QuestionDTO> addQuestion(@Valid @RequestBody CreateQuestionRequest request) {
        return ResponseEntity.status(201).body(questionService.addQuestion(request));
    }

    @GetMapping("generate")
    public ResponseEntity<List<Integer>> getQuestionsForQuiz(@RequestParam @NotBlank String categoryName,
            @RequestParam @Min(1) @Max(50) Integer numQuestions) {
        return ResponseEntity.ok(questionService.getQuestionsForQuiz(categoryName, numQuestions));
    }

    @PostMapping("getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestBody List<Integer> questionIds) {
        return ResponseEntity.ok(questionService.getQuestionsFromId(questionIds));
    }

    @PostMapping("getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses) {
        return ResponseEntity.ok(questionService.getScore(responses));
    }

}
