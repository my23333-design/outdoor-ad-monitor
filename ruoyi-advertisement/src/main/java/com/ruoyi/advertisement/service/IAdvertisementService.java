package com.ruoyi.advertisement.service;

import java.util.List;
import com.ruoyi.advertisement.domain.Advertisement;

/**
 * 广告列表Service接口
 * 
 * @author ruoyi
 * @date 2026-03-18
 */
public interface IAdvertisementService 
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
     * 批量删除广告列表
     * 
     * @param ids 需要删除的广告列表主键集合
     * @return 结果
     */
    public int deleteAdvertisementByIds(Long[] ids);

    /**
     * 删除广告列表信息
     * 
     * @param id 广告列表主键
     * @return 结果
     */
    public int deleteAdvertisementById(Long id);

    /**
     * 批量重置所有广告审核状态为未审核
     * 
     * @return 结果
     */
    public int resetAllAdvertisementStatus();
}
