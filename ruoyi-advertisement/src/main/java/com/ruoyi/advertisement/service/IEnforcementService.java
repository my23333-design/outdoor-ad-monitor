package com.ruoyi.advertisement.service;

import java.util.List;
import com.ruoyi.advertisement.domain.Enforcement;

/**
 * 执行结果记录Service接口
 * 
 * @author ruoyi
 * @date 2026-03-18
 */
public interface IEnforcementService 
{
    /**
     * 查询执行结果记录
     * 
     * @param id 执行结果记录主键
     * @return 执行结果记录
     */
    public Enforcement selectEnforcementById(Long id);

    /**
     * 查询执行结果记录列表
     * 
     * @param enforcement 执行结果记录
     * @return 执行结果记录集合
     */
    public List<Enforcement> selectEnforcementList(Enforcement enforcement);

    /**
     * 新增执行结果记录
     * 
     * @param enforcement 执行结果记录
     * @return 结果
     */
    public int insertEnforcement(Enforcement enforcement);

    /**
     * 修改执行结果记录
     * 
     * @param enforcement 执行结果记录
     * @return 结果
     */
    public int updateEnforcement(Enforcement enforcement);

    /**
     * 批量删除执行结果记录
     * 
     * @param ids 需要删除的执行结果记录主键集合
     * @return 结果
     */
    public int deleteEnforcementByIds(Long[] ids);

    /**
     * 删除执行结果记录信息
     * 
     * @param id 执行结果记录主键
     * @return 结果
     */
    public int deleteEnforcementById(Long id);
}
