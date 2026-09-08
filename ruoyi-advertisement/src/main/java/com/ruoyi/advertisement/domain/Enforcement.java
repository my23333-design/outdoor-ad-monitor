package com.ruoyi.advertisement.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 执行结果记录对象 tb_enforcement
 * 
 * @author ruoyi
 * @date 2026-03-18
 */
public class Enforcement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 处理Id */
    private Long id;

    /** 广告Id */
    @Excel(name = "广告Id")
    private Long adId;

    /** 处理结果 */
    @Excel(name = "处理结果")
    private String handleResult;

    /** 处理后照片 */
    @Excel(name = "处理后照片")
    private String postHandleImage;

    /** 处理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date handleTime;

    /** 处理者 */
    @Excel(name = "处理者")
    private String handler;

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

    public void setHandleResult(String handleResult) 
    {
        this.handleResult = handleResult;
    }

    public String getHandleResult() 
    {
        return handleResult;
    }

    public void setPostHandleImage(String postHandleImage) 
    {
        this.postHandleImage = postHandleImage;
    }

    public String getPostHandleImage() 
    {
        return postHandleImage;
    }

    public void setHandleTime(Date handleTime) 
    {
        this.handleTime = handleTime;
    }

    public Date getHandleTime() 
    {
        return handleTime;
    }

    public void setHandler(String handler) 
    {
        this.handler = handler;
    }

    public String getHandler() 
    {
        return handler;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("adId", getAdId())
            .append("handleResult", getHandleResult())
            .append("postHandleImage", getPostHandleImage())
            .append("handleTime", getHandleTime())
            .append("handler", getHandler())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
