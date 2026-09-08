package com.ruoyi.advertisement.domain;

import java.util.Date;

/**
 * AI审核结果
 */
public class AIReviewResult {
    private Long id;
    private Long adId;
    private String textReviewResult;
    private String imageReviewResult;
    private String textDetail;
    private String imageDetail;
    private String overallResult;
    private Date reviewTime;
    private Date createTime;
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdId() {
        return adId;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }

    public String getTextReviewResult() {
        return textReviewResult;
    }

    public void setTextReviewResult(String textReviewResult) {
        this.textReviewResult = textReviewResult;
    }

    public String getImageReviewResult() {
        return imageReviewResult;
    }

    public void setImageReviewResult(String imageReviewResult) {
        this.imageReviewResult = imageReviewResult;
    }

    public String getTextDetail() {
        return textDetail;
    }

    public void setTextDetail(String textDetail) {
        this.textDetail = textDetail;
    }

    public String getImageDetail() {
        return imageDetail;
    }

    public void setImageDetail(String imageDetail) {
        this.imageDetail = imageDetail;
    }

    public String getOverallResult() {
        return overallResult;
    }

    public void setOverallResult(String overallResult) {
        this.overallResult = overallResult;
    }

    public Date getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}