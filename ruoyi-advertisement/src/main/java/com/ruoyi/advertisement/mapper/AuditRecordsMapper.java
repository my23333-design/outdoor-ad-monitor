package com.ruoyi.advertisement.mapper;

import java.util.List;
import com.ruoyi.advertisement.domain.AuditRecords;

/**
 * 审核记录Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-18
 */
public interface AuditRecordsMapper 
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
     * 删除审核记录
     * 
     * @param id 审核记录主键
     * @return 结果
     */
    public int deleteAuditRecordsById(Long id);

    /**
     * 批量删除审核记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAuditRecordsByIds(Long[] ids);
}
