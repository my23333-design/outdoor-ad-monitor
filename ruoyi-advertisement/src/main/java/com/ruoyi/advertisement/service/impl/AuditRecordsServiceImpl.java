package com.ruoyi.advertisement.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.advertisement.mapper.AuditRecordsMapper;
import com.ruoyi.advertisement.domain.AuditRecords;
import com.ruoyi.advertisement.service.IAuditRecordsService;

/**
 * 审核记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-18
 */
@Service
public class AuditRecordsServiceImpl implements IAuditRecordsService 
{
    @Autowired
    private AuditRecordsMapper auditRecordsMapper;

    /**
     * 查询审核记录
     * 
     * @param id 审核记录主键
     * @return 审核记录
     */
    @Override
    public AuditRecords selectAuditRecordsById(Long id)
    {
        return auditRecordsMapper.selectAuditRecordsById(id);
    }

    /**
     * 查询审核记录列表
     * 
     * @param auditRecords 审核记录
     * @return 审核记录
     */
    @Override
    public List<AuditRecords> selectAuditRecordsList(AuditRecords auditRecords)
    {
        return auditRecordsMapper.selectAuditRecordsList(auditRecords);
    }

    /**
     * 新增审核记录
     * 
     * @param auditRecords 审核记录
     * @return 结果
     */
    @Override
    public int insertAuditRecords(AuditRecords auditRecords)
    {
        auditRecords.setCreateTime(DateUtils.getNowDate());
        return auditRecordsMapper.insertAuditRecords(auditRecords);
    }

    /**
     * 修改审核记录
     * 
     * @param auditRecords 审核记录
     * @return 结果
     */
    @Override
    public int updateAuditRecords(AuditRecords auditRecords)
    {
        auditRecords.setUpdateTime(DateUtils.getNowDate());
        return auditRecordsMapper.updateAuditRecords(auditRecords);
    }

    /**
     * 批量删除审核记录
     * 
     * @param ids 需要删除的审核记录主键
     * @return 结果
     */
    @Override
    public int deleteAuditRecordsByIds(Long[] ids)
    {
        return auditRecordsMapper.deleteAuditRecordsByIds(ids);
    }

    /**
     * 删除审核记录信息
     * 
     * @param id 审核记录主键
     * @return 结果
     */
    @Override
    public int deleteAuditRecordsById(Long id)
    {
        return auditRecordsMapper.deleteAuditRecordsById(id);
    }
}
