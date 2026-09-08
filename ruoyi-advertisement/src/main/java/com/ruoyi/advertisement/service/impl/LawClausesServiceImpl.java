package com.ruoyi.advertisement.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.advertisement.mapper.LawClausesMapper;
import com.ruoyi.advertisement.domain.LawClauses;
import com.ruoyi.advertisement.service.ILawClausesService;

/**
 * 广告法律条款管理Service业务层处理
 * 
 * @author wanghao
 * @date 2025-12-08
 */
@Service
public class LawClausesServiceImpl implements ILawClausesService 
{
    @Autowired
    private LawClausesMapper lawClausesMapper;

    /**
     * 查询广告法律条款管理
     * 
     * @param id 广告法律条款管理主键
     * @return 广告法律条款管理
     */
    @Override
    public LawClauses selectLawClausesById(Long id)
    {
        return lawClausesMapper.selectLawClausesById(id);
    }

    /**
     * 查询广告法律条款管理列表
     * 
     * @param lawClauses 广告法律条款管理
     * @return 广告法律条款管理
     */
    @Override
    public List<LawClauses> selectLawClausesList(LawClauses lawClauses)
    {
        return lawClausesMapper.selectLawClausesList(lawClauses);
    }

    /**
     * 新增广告法律条款管理
     * 
     * @param lawClauses 广告法律条款管理
     * @return 结果
     */
    @Override
    public int insertLawClauses(LawClauses lawClauses)
    {
        // 添加校验，验证法律法规编码是否重复（只查询未删除的数据）
        LawClauses lawClauses1 = new LawClauses();
        lawClauses1.setClauseCode(lawClauses.getClauseCode());
        lawClauses1.setIsDeleted(0);
        List<LawClauses> list = lawClausesMapper.selectLawClausesList(lawClauses1);
        if(list != null && list.size() > 0){
            return -1;
        }
        lawClauses.setCreateTime(DateUtils.getNowDate());
        return lawClausesMapper.insertLawClauses(lawClauses);
    }

    /**
     * 修改广告法律条款管理
     * 
     * @param lawClauses 广告法律条款管理
     * @return 结果
     */
    @Override
    public int updateLawClauses(LawClauses lawClauses)
    {
        // 修改时校验法律编号是否重复（排除自身，只查询未删除的数据）
        LawClauses lawClauses1 = new LawClauses();
        lawClauses1.setClauseCode(lawClauses.getClauseCode());
        lawClauses1.setIsDeleted(0);
        List<LawClauses> list = lawClausesMapper.selectLawClausesList(lawClauses1);
        if(list != null && list.size() > 0){
            // 排除自身，检查是否有其他记录使用相同的法律编号
            for(LawClauses item : list){
                if(!item.getId().equals(lawClauses.getId())){
                    return -1; // 法律编号已存在
                }
            }
        }
        lawClauses.setUpdateTime(DateUtils.getNowDate());
        return lawClausesMapper.updateLawClauses(lawClauses);
    }

    /**
     * 批量删除广告法律条款管理
     * 
     * @param ids 需要删除的广告法律条款管理主键
     * @return 结果
     */
    @Override
    public int deleteLawClausesByIds(Long[] ids)
    {
        return lawClausesMapper.deleteLawClausesByIds(ids);
    }

    /**
     * 删除广告法律条款管理信息
     * 
     * @param id 广告法律条款管理主键
     * @return 结果
     */
    @Override
    public int deleteLawClausesById(Long id)
    {
        return lawClausesMapper.deleteLawClausesById(id);
    }
    @Override
    public int deleteLawClausesByIdsAndIsDelete(Long[] ids) {
        //逻辑删除
        LawClauses lawClauses = new LawClauses();
        lawClauses.setIsDeleted(1);
        lawClauses.setId(ids[0]);
    return updateLawClauses(lawClauses);
    }
}
