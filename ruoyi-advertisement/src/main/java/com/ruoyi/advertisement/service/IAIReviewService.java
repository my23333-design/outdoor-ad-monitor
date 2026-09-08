package com.ruoyi.advertisement.service;

/**
 * AI审核服务接口
 * 
 * @author ruoyi
 */
public interface IAIReviewService {

    /**
     * 自动审核广告
     * 根据广告ID查询数据，执行百度AI图片审核，更新广告状态
     * 
     * @param id 广告ID
     * @return 审核结果描述
     */
    String autoAuditAdvertisement(Long id);

    /**
     * 审核图片
     * 
     * @param imagePath 图片路径（URL或本地路径）
     * @return 审核结果
     */
    String reviewImage(String imagePath);
}
