package com.ruoyi.advertisement.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 审核记录对象 tb_audit_records
 * 
 * @author ruoyi
 * @date 2026-03-18
 */
public class AuditRecords extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 广告ID */
    @Excel(name = "广告ID")
    private Long adId;

    /** 审核状态 */
    @Excel(name = "审核状态")
    private String auditStatus;

    /** 当审核状态是违法、存疑状态，可以选择法条数据,可以多条逗号隔开 */
    @Excel(name = "当审核状态是违法、存疑状态，可以选择法条数据,可以多条逗号隔开")
    private String clauseCode;

    /** 审核意见 */
    @Excel(name = "审核意见")
    private String auditComments;

    /** 审核人 */
    @Excel(name = "审核人")
    private String auditor;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date auditTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setAdId(Long adId) 
    {
        this.adId = adId;
    }

    public Long getAdId() 
    {
        return adId;
    }

    public void setAuditStatus(String auditStatus) 
    {
        this.auditStatus = auditStatus;
    }

    public String getAuditStatus() 
    {
        return auditStatus;
    }

    public void setClauseCode(String clauseCode) 
    {
        this.clauseCode = clauseCode;
    }

    public String getClauseCode() 
    {
        return clauseCode;
    }

    public void setAuditComments(String auditComments) 
    {
        this.auditComments = auditComments;
    }

    public String getAuditComments() 
    {
        return auditComments;
    }

    public void setAuditor(String auditor) 
    {
        this.auditor = auditor;
    }

    public String getAuditor() 
    {
        return auditor;
    }

    public void setAuditTime(Date auditTime) 
    {
        this.auditTime = auditTime;
    }

    public Date getAuditTime() 
    {
        return auditTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("adId", getAdId())
            .append("auditStatus", getAuditStatus())
            .append("clauseCode", getClauseCode())
            .append("auditComments", getAuditComments())
            .append("auditor", getAuditor())
            .append("auditTime", getAuditTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
