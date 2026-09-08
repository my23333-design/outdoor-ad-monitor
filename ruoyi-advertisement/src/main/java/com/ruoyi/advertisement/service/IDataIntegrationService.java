package com.ruoyi.advertisement.service;

import com.ruoyi.common.core.domain.AjaxResult;

public interface IDataIntegrationService {

    AjaxResult getIntegratedStatistics();

    AjaxResult getDistrictHeatmapData();

    AjaxResult getIllegalDistrictData();

    AjaxResult getIllegalIndustryData();

    AjaxResult getMediumTypeData();

    AjaxResult getMonthlyAdTypeData();

    AjaxResult getDistrictStatusData();

    AjaxResult getLatestIllegalData();

    AjaxResult getOnlineRateData();

    AjaxResult getPlayTimeData();

    AjaxResult getAllData();

    void forceSyncData();

    AjaxResult getSyncStatus();
}