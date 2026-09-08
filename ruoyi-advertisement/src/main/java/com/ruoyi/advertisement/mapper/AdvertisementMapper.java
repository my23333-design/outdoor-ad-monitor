package com.ruoyi.advertisement.mapper;

import java.util.List;
import com.ruoyi.advertisement.domain.Advertisement;
import com.ruoyi.advertisement.domain.AIReviewResult;

/**
 * 广告列表Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-18
 */
public interface AdvertisementMapper 
{
    /**
     * 查询广告列表
     * 
     * @param id 广告列表主键
     * @return 广告列表
     */
    public Advertisement selectAdvertisementById(Long id);

    /**
     * 查询广告列表列表
     * 
     * @param advertisement 广告列表
     * @return 广告列表集合
     */
    public List<Advertisement> selectAdvertisementList(Advertisement advertisement);

    /**
     * 新增广告列表
     * 
     * @param advertisement 广告列表
     * @return 结果
     */
    public int insertAdvertisement(Advertisement advertisement);

    /**
     * 修改广告列表
     * 
     * @param advertisement 广告列表
     * @return 结果
     */
    public int updateAdvertisement(Advertisement advertisement);

    /**
     * 删除广告列表
     * 
     * @param id 广告列表主键
     * @return 结果
     */
    public int deleteAdvertisementById(Long id);

    /**
     * 批量删除广告列表
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAdvertisementByIds(Long[] ids);

    /**
     * 查询AI审核结果
     * 
     * @param adId 广告ID
     * @return AI审核结果
     */
    public AIReviewResult selectAIReviewResultByAdId(Long adId);

    /**
     * 新增AI审核结果
     * 
     * @param reviewResult AI审核结果
     * @return 结果
     */
    public int insertAIReviewResult(AIReviewResult reviewResult);

    /**
     * 修改AI审核结果
     * 
     * @param reviewResult AI审核结果
     * @return 结果
     */
    public int updateAIReviewResult(AIReviewResult reviewResult);

    /**
     * 批量重置所有广告审核状态为未审核
     * 
     * @return 结果
     */
    public int resetAllAdvertisementStatus();
}
