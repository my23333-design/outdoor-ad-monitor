package com.ruoyi.advertisement.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 广告列表对象 tb_advertisement
 * 
 * @author ruoyi
 * @date 2026-03-18
 */
public class Advertisement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 营利类型 */
    @Excel(name = "营利类型")
    private String adProfitabilityType;

    /** 行业分类 */
    @Excel(name = "行业分类")
    private String adIndustryType;

    /** 媒体类型 */
    @Excel(name = "媒体类型")
    private String adMediumType;

    /** 违法行为描述 */
    @Excel(name = "违法行为描述")
    private String adDescription;

    /** 广告图片路径 */
    @Excel(name = "广告图片路径")
    private String adImages;

    /** 违规类别 */
    @Excel(name = "违规类别")
    private String violationType;

    /** 省  */
    @Excel(name = "省 ")
    private String province;

    /** 市  */
    @Excel(name = "市 ")
    private String city;

    /** 区/县 */
    @Excel(name = "区/县")
    private String district;

    /** 街道 */
    @Excel(name = "街道")
    private String street;

    /** 详细地址 */
    @Excel(name = "详细地址")
    private String address;

    /** 经度 */
    @Excel(name = "经度")
    private String latitude;

    /** 维度 */
    @Excel(name = "维度")
    private String longitude;

    /** 广告主 */
    @Excel(name = "广告主")
    private String advertiser;

    /** 采集时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "采集时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date surveyTime;

    /** 监测人 */
    @Excel(name = "监测人")
    private String surveyor;

    /** 查看状态 */
    @Excel(name = "查看状态")
    private String checkStatus;

    /** 查看时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "查看时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date checkTime;

    /** 处理状态  */
    @Excel(name = "处理状态 ")
    private String handleStatus;

    /** 审核状态 */
    @Excel(name = "审核状态")
    private String auditStatus;

    /** 报告 */
    @Excel(name = "报告")
    private String report;

    /** 逻辑删除标志 */
    @Excel(name = "逻辑删除标志")
    private Integer isDeleted;

    /** 处理结果 */
    @Excel(name = "处理结果")
    private String processResult;

    /** 处理结果名称 */
    @Excel(name = "处理结果名称")
    private String processResultName;

    /** 处理人 */
    @Excel(name = "处理人")
    private String processPerson;

    /** 处理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd")
    private java.util.Date processTime;

    /** 处理后图片 */
    @Excel(name = "处理后图片")
    private String afterImages;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setAdProfitabilityType(String adProfitabilityType) 
    {
        this.adProfitabilityType = adProfitabilityType;
    }

    public String getAdProfitabilityType() 
    {
        return adProfitabilityType;
    }

    public void setAdIndustryType(String adIndustryType) 
    {
        this.adIndustryType = adIndustryType;
    }

    public String getAdIndustryType() 
    {
        return adIndustryType;
    }

    public void setAdMediumType(String adMediumType) 
    {
        this.adMediumType = adMediumType;
    }

    public String getAdMediumType() 
    {
        return adMediumType;
    }

    public void setAdDescription(String adDescription) 
    {
        this.adDescription = adDescription;
    }

    public String getAdDescription() 
    {
        return adDescription;
    }

    public void setAdImages(String adImages) 
    {
        this.adImages = adImages;
    }

    public String getAdImages() 
    {
        return adImages;
    }

    public void setViolationType(String violationType) 
    {
        this.violationType = violationType;
    }

    public String getViolationType() 
    {
        return violationType;
    }

    public void setProvince(String province) 
    {
        this.province = province;
    }

    public String getProvince() 
    {
        return province;
    }

    public void setCity(String city) 
    {
        this.city = city;
    }

    public String getCity() 
    {
        return city;
    }

    public void setDistrict(String district) 
    {
        this.district = district;
    }

    public String getDistrict() 
    {
        return district;
    }

    public void setStreet(String street) 
    {
        this.street = street;
    }

    public String getStreet() 
    {
        return street;
    }

    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
    }

    public void setLatitude(String latitude) 
    {
        this.latitude = latitude;
    }

    public String getLatitude() 
    {
        return latitude;
    }

    public void setLongitude(String longitude) 
    {
        this.longitude = longitude;
    }

    public String getLongitude() 
    {
        return longitude;
    }

    public void setAdvertiser(String advertiser) 
    {
        this.advertiser = advertiser;
    }

    public String getAdvertiser() 
    {
        return advertiser;
    }

    public void setSurveyTime(Date surveyTime) 
    {
        this.surveyTime = surveyTime;
    }

    public Date getSurveyTime() 
    {
        return surveyTime;
    }

    public void setSurveyor(String surveyor) 
    {
        this.surveyor = surveyor;
    }

    public String getSurveyor() 
    {
        return surveyor;
    }

    public void setCheckStatus(String checkStatus) 
    {
        this.checkStatus = checkStatus;
    }

    public String getCheckStatus() 
    {
        return checkStatus;
    }

    public void setCheckTime(Date checkTime) 
    {
        this.checkTime = checkTime;
    }

    public Date getCheckTime() 
    {
        return checkTime;
    }

    public void setHandleStatus(String handleStatus) 
    {
        this.handleStatus = handleStatus;
    }

    public String getHandleStatus() 
    {
        return handleStatus;
    }

    public void setAuditStatus(String auditStatus) 
    {
        this.auditStatus = auditStatus;
    }

    public String getAuditStatus() 
    {
        return auditStatus;
    }

    public void setReport(String report) 
    {
        this.report = report;
    }

    public String getReport() 
    {
        return report;
    }

    public void setIsDeleted(Integer isDeleted) 
    {
        this.isDeleted = isDeleted;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setProcessResult(String processResult) {
        this.processResult = processResult;
    }

    public String getProcessResult() {
        return processResult;
    }

    public void setProcessResultName(String processResultName) {
        this.processResultName = processResultName;
    }

    public String getProcessResultName() {
        return processResultName;
    }

    public void setProcessPerson(String processPerson) {
        this.processPerson = processPerson;
    }

    public String getProcessPerson() {
        return processPerson;
    }

    public void setProcessTime(java.util.Date processTime) {
        this.processTime = processTime;
    }

    public java.util.Date getProcessTime() {
        return processTime;
    }

    public void setAfterImages(String afterImages) {
        this.afterImages = afterImages;
    }

    public String getAfterImages() {
        return afterImages;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("adProfitabilityType", getAdProfitabilityType())
            .append("adIndustryType", getAdIndustryType())
            .append("adMediumType", getAdMediumType())
            .append("adDescription", getAdDescription())
            .append("adImages", getAdImages())
            .append("violationType", getViolationType())
            .append("province", getProvince())
            .append("city", getCity())
            .append("district", getDistrict())
            .append("street", getStreet())
            .append("address", getAddress())
            .append("latitude", getLatitude())
            .append("longitude", getLongitude())
            .append("advertiser", getAdvertiser())
            .append("surveyTime", getSurveyTime())
            .append("surveyor", getSurveyor())
            .append("checkStatus", getCheckStatus())
            .append("checkTime", getCheckTime())
            .append("handleStatus", getHandleStatus())
            .append("auditStatus", getAuditStatus())
            .append("report", getReport())
            .append("isDeleted", getIsDeleted())
            .append("processResult", getProcessResult())
            .append("processResultName", getProcessResultName())
            .append("processPerson", getProcessPerson())
            .append("processTime", getProcessTime())
            .append("afterImages", getAfterImages())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
