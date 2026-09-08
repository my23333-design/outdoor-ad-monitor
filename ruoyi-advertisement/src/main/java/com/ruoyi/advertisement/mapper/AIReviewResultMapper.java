package com.ruoyi.advertisement.mapper;

import com.ruoyi.advertisement.domain.AIReviewResult;

/**
 * AI审核结果Mapper接口
 */
public interface AIReviewResultMapper {
    /**
     * 插入AI审核结果
     * @param aiReviewResult AI审核结果
     * @return 影响行数
     */
    int insertAIReviewResult(AIReviewResult aiReviewResult);

    /**
     * 更新AI审核结果
     * @param aiReviewResult AI审核结果
     * @return 影响行数
     */
    int updateAIReviewResult(AIReviewResult aiReviewResult);

    /**
     * 根据广告ID查询AI审核结果
     * @param adId 广告ID
     * @return AI审核结果
     */
    AIReviewResult selectAIReviewResultByAdId(Long adId);
}