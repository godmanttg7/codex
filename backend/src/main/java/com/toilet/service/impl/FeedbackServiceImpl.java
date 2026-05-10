package com.toilet.service.impl;

import com.toilet.dto.FeedbackCreateRequest;
import com.toilet.dto.StatsResponse;
import com.toilet.entity.Feedback;
import com.toilet.mapper.FeedbackMapper;
import com.toilet.service.FeedbackService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackMapper feedbackMapper;

    public FeedbackServiceImpl(FeedbackMapper feedbackMapper) {
        this.feedbackMapper = feedbackMapper;
    }

    @Override
    public void create(FeedbackCreateRequest request) {
        Feedback feedback = new Feedback();
        feedback.setToiletName(request.getToiletName());
        feedback.setArea(request.getArea());
        feedback.setCleanlinessScore(request.getCleanlinessScore());
        feedback.setQueueScore(request.getQueueScore());
        feedback.setOdorScore(request.getOdorScore());
        feedback.setComment(request.getComment());
        feedback.setVisitedAt(request.getVisitedAt());
        feedbackMapper.insert(feedback);
    }

    @Override
    public List<Feedback> listLatest(Integer limit) {
        int safeLimit = (limit == null || limit <= 0 || limit > 100) ? 20 : limit;
        return feedbackMapper.listLatest(safeLimit);
    }

    @Override
    public StatsResponse getStats() {
        StatsResponse stats = feedbackMapper.getStats();
        if (stats == null || stats.getTotalCount() == null || stats.getTotalCount() == 0) {
            StatsResponse empty = new StatsResponse();
            empty.setTotalCount(0L);
            empty.setAvgCleanliness(0D);
            empty.setAvgQueue(0D);
            empty.setAvgOdor(0D);
            return empty;
        }
        return stats;
    }
}
