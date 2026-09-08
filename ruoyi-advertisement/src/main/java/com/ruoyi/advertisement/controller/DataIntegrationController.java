package com.ruoyi.advertisement.controller;

import com.ruoyi.advertisement.service.IDataIntegrationService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 广告数据集成Controller
 */
@RestController
@RequestMapping("/advertisement/dataIntegration")
public class DataIntegrationController extends BaseController {

    @Autowired
    private IDataIntegrationService dataIntegrationService;

    /**
     * 获取所有数据
     * @return 所有数据
     */
    @GetMapping("/getAllData")
    public AjaxResult getAllData() {
        return dataIntegrationService.getAllData();
    }

    /**
     * 获取统计数据
     * @return 统计数据
     */
    @GetMapping("/getStatistics")
    public AjaxResult getStatistics() {
        return dataIntegrationService.getIntegratedStatistics();
    }

    /**
     * 获取热力图数据
     * @return 热力图数据
     */
    @GetMapping("/getHeatmapData")
    public AjaxResult getHeatmapData() {
        return dataIntegrationService.getDistrictHeatmapData();
    }

    /**
     * 获取违法区域数据
     * @return 违法区域数据
     */
    @GetMapping("/getIllegalDistrictData")
    public AjaxResult getIllegalDistrictData() {
        return dataIntegrationService.getIllegalDistrictData();
    }

    /**
     * 获取违法行业数据
     * @return 违法行业数据
     */
    @GetMapping("/getIllegalIndustryData")
    public AjaxResult getIllegalIndustryData() {
        return dataIntegrationService.getIllegalIndustryData();
    }

    /**
     * 获取媒体类型数据
     * @return 媒体类型数据
     */
    @GetMapping("/getMediumTypeData")
    public AjaxResult getMediumTypeData() {
        return dataIntegrationService.getMediumTypeData();
    }

    /**
     * 获取月度广告类型数据
     * @return 月度广告类型数据
     */
    @GetMapping("/getMonthlyAdTypeData")
    public AjaxResult getMonthlyAdTypeData() {
        return dataIntegrationService.getMonthlyAdTypeData();
    }

    /**
     * 获取区域状态数据
     * @return 区域状态数据
     */
    @GetMapping("/getDistrictStatusData")
    public AjaxResult getDistrictStatusData() {
        return dataIntegrationService.getDistrictStatusData();
    }

    /**
     * 获取最新违法数据
     * @return 最新违法数据
     */
    @GetMapping("/getLatestIllegalData")
    public AjaxResult getLatestIllegalData() {
        return dataIntegrationService.getLatestIllegalData();
    }

    /**
     * 获取在线率数据
     * @return 在线率数据
     */
    @GetMapping("/getOnlineRateData")
    public AjaxResult getOnlineRateData() {
        return dataIntegrationService.getOnlineRateData();
    }

    /**
     * 获取播放时长数据
     * @return 播放时长数据
     */
    @GetMapping("/getPlayTimeData")
    public AjaxResult getPlayTimeData() {
        return dataIntegrationService.getPlayTimeData();
    }

    /**
     * 强制同步数据
     * @return 同步结果
     */
    @GetMapping("/forceSync")
    public AjaxResult forceSync() {
        dataIntegrationService.forceSyncData();
        return success("数据同步成功");
    }

    /**
     * 获取数据同步状态
     * @return 同步状态
     */
    @GetMapping("/getSyncStatus")
    public AjaxResult getSyncStatus() {
        return dataIntegrationService.getSyncStatus();
    }
}