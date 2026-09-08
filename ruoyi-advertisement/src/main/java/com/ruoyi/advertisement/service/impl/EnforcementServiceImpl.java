package com.ruoyi.advertisement.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.advertisement.mapper.EnforcementMapper;
import com.ruoyi.advertisement.domain.Enforcement;
import com.ruoyi.advertisement.service.IEnforcementService;

/**
 * 执行结果记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-18
 */
@Service
public class EnforcementServiceImpl implements IEnforcementService 
{
    @Autowired
    private EnforcementMapper enforcementMapper;

    /**
     * 查询执行结果记录
     * 
     * @param id 执行结果记录主键
     * @return 执行结果记录
     */
    @Override
    public Enforcement selectEnforcementById(Long id)
    {
        return enforcementMapper.selectEnforcementById(id);
    }

    /**
     * 查询执行结果记录列表
     * 
     * @param enforcement 执行结果记录
     * @return 执行结果记录
     */
    @Override
    public List<Enforcement> selectEnforcementList(Enforcement enforcement)
    {
        return enforcementMapper.selectEnforcementList(enforcement);
    }

    /**
     * 新增执行结果记录
     * 
     * @param enforcement 执行结果记录
     * @return 结果
     */
    @Override
    public int insertEnforcement(Enforcement enforcement)
    {
        enforcement.setCreateTime(DateUtils.getNowDate());
        return enforcementMapper.insertEnforcement(enforcement);
    }

    /**
     * 修改执行结果记录
     * 
     * @param enforcement 执行结果记录
     * @return 结果
     */
    @Override
    public int updateEnforcement(Enforcement enforcement)
    {
        enforcement.setUpdateTime(DateUtils.getNowDate());
        return enforcementMapper.updateEnforcement(enforcement);
    }

    /**
     * 批量删除执行结果记录
     * 
     * @param ids 需要删除的执行结果记录主键
     * @return 结果
     */
    @Override
    public int deleteEnforcementByIds(Long[] ids)
    {
        return enforcementMapper.deleteEnforcementByIds(ids);
    }

    /**
     * 删除执行结果记录信息
     * 
     * @param id 执行结果记录主键
     * @return 结果
     */
    @Override
    public int deleteEnforcementById(Long id)
    {
        return enforcementMapper.deleteEnforcementById(id);
    }
}
