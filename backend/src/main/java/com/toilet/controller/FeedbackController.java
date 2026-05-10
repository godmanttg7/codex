package com.toilet.controller;

import com.toilet.dto.ApiResponse;
import com.toilet.dto.FeedbackCreateRequest;
import com.toilet.dto.StatsResponse;
import com.toilet.entity.Feedback;
import com.toilet.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ApiResponse<Void> create(@Valid @RequestBody FeedbackCreateRequest request) {
        feedbackService.create(request);
        return ApiResponse.ok(null);
    }

    @GetMapping("/latest")
    public ApiResponse<List<Feedback>> latest(@RequestParam(required = false) Integer limit) {
        return ApiResponse.ok(feedbackService.listLatest(limit));
    }

    @GetMapping("/stats")
    public ApiResponse<StatsResponse> stats() {
        return ApiResponse.ok(feedbackService.getStats());
    }
}
