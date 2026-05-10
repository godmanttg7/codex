package com.toilet.service;

import com.toilet.dto.FeedbackCreateRequest;
import com.toilet.dto.StatsResponse;
import com.toilet.entity.Feedback;

import java.util.List;

public interface FeedbackService {
    void create(FeedbackCreateRequest request);
    List<Feedback> listLatest(Integer limit);
    StatsResponse getStats();
}
