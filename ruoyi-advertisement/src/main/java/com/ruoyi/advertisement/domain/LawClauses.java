package com.ruoyi.advertisement.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 广告法律条款管理对象 tb_law_clauses
 * 
 * @author wanghao
 * @date 2025-12-08
 */
public class LawClauses extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 法条编码  */
    @Excel(name = "法条编码 ")
    private String clauseCode;

    /** 法律名称 */
    @Excel(name = "法律名称")
    private String lawName;

    /** 条款编号 */
    @Excel(name = "条款编号")
    private String clauseNumber;

    /** 条款内容 */
    @Excel(name = "条款内容")
    private String content;

    /** 逻辑删除标志 */
    @Excel(name = "逻辑删除标志")
    private Integer isDeleted;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setClauseCode(String clauseCode) 
    {
        this.clauseCode = clauseCode;
    }

    public String getClauseCode() 
    {
        return clauseCode;
    }

    public void setLawName(String lawName) 
    {
        this.lawName = lawName;
    }

    public String getLawName() 
    {
        return lawName;
    }

    public void setClauseNumber(String clauseNumber) 
    {
        this.clauseNumber = clauseNumber;
    }

    public String getClauseNumber() 
    {
        return clauseNumber;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setIsDeleted(Integer isDeleted) 
    {
        this.isDeleted = isDeleted;
    }

    public Integer getIsDeleted() 
    {
        return isDeleted;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("clauseCode", getClauseCode())
            .append("lawName", getLawName())
            .append("clauseNumber", getClauseNumber())
            .append("content", getContent())
            .append("isDeleted", getIsDeleted())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
