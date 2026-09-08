package com.ruoyi.advertisement.service;

import java.util.List;
import com.ruoyi.advertisement.domain.AuditRecords;

/**
 * 审核记录Service接口
 * 
 * @author ruoyi
 * @date 2026-03-18
 */
public interface IAuditRecordsService 
{
    /**
     * 查询审核记录
     * 
     * @param id 审核记录主键
     * @return 审核记录
     */
    public AuditRecords selectAuditRecordsById(Long id);

    /**
     * 查询审核记录列表
     * 
     * @param auditRecords 审核记录
     * @return 审核记录集合
     */
    public List<AuditRecords> selectAuditRecordsList(AuditRecords auditRecords);

    /**
     * 新增审核记录
     * 
     * @param auditRecords 审核记录
     * @return 结果
     */
    public int insertAuditRecords(AuditRecords auditRecords);

    /**
     * 修改审核记录
     * 
     * @param auditRecords 审核记录
     * @return 结果
     */
    public int updateAuditRecords(AuditRecords auditRecords);

    /**
     * 批量删除审核记录
     * 
     * @param ids 需要删除的审核记录主键集合
     * @return 结果
     */
    public int deleteAuditRecordsByIds(Long[] ids);

    /**
     * 删除审核记录信息
     * 
     * @param id 审核记录主键
     * @return 结果
     */
    public int deleteAuditRecordsById(Long id);
}
