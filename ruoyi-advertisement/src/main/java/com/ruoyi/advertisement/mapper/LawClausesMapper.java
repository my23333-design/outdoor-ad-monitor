package com.ruoyi.advertisement.mapper;

import java.util.List;
import com.ruoyi.advertisement.domain.LawClauses;

/**
 * 广告法律条款管理Mapper接口
 * 
 * @author wanghao
 * @date 2025-12-08
 */
public interface LawClausesMapper 
{
    /**
     * 查询广告法律条款管理
     * 
     * @param id 广告法律条款管理主键
     * @return 广告法律条款管理
     */
    public LawClauses selectLawClausesById(Long id);

    /**
     * 查询广告法律条款管理列表
     * 
     * @param lawClauses 广告法律条款管理
     * @return 广告法律条款管理集合
     */
    public List<LawClauses> selectLawClausesList(LawClauses lawClauses);

    /**
     * 新增广告法律条款管理
     * 
     * @param lawClauses 广告法律条款管理
     * @return 结果
     */
    public int insertLawClauses(LawClauses lawClauses);

    /**
     * 修改广告法律条款管理
     * 
     * @param lawClauses 广告法律条款管理
     * @return 结果
     */
    public int updateLawClauses(LawClauses lawClauses);

    /**
     * 删除广告法律条款管理
     * 
     * @param id 广告法律条款管理主键
     * @return 结果
     */
    public int deleteLawClausesById(Long id);

    /**
     * 批量删除广告法律条款管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLawClausesByIds(Long[] ids);
}
