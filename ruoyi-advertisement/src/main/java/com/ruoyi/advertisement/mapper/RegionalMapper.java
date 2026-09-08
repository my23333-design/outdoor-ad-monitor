package com.ruoyi.advertisement.mapper;

import java.util.List;
import com.ruoyi.advertisement.domain.Regional;

/**
 * 行政区域管理Mapper接口
 * 
 * @author wanghao
 * @date 2025-12-04
 */
public interface RegionalMapper 
{
    /**
     * 查询行政区域管理
     * 
     * @param code 行政区域管理主键
     * @return 行政区域管理
     */
    public Regional selectRegionalByCode(String code);

    /**
     * 查询行政区域管理列表
     * 
     * @param regional 行政区域管理
     * @return 行政区域管理集合
     */
    public List<Regional> selectRegionalList(Regional regional);

    /**
     * 新增行政区域管理
     * 
     * @param regional 行政区域管理
     * @return 结果
     */
    public int insertRegional(Regional regional);

    /**
     * 修改行政区域管理
     * 
     * @param regional 行政区域管理
     * @return 结果
     */
    public int updateRegional(Regional regional);

    /**
     * 删除行政区域管理
     * 
     * @param code 行政区域管理主键
     * @return 结果
     */
    public int deleteRegionalByCode(String code);

    /**
     * 批量删除行政区域管理
     * 
     * @param codes 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRegionalByCodes(String[] codes);
}
