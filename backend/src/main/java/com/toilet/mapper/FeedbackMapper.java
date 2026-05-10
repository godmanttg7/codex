package com.toilet.mapper;

import com.toilet.dto.StatsResponse;
import com.toilet.entity.Feedback;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FeedbackMapper {
    int insert(Feedback feedback);
    List<Feedback> listLatest(@Param("limit") Integer limit);
    StatsResponse getStats();
}
