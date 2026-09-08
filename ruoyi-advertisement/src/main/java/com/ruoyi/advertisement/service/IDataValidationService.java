package com.ruoyi.advertisement.service;

import com.ruoyi.advertisement.domain.Advertisement;

import java.util.List;
import java.util.Map;

public interface IDataValidationService {

    Map<String, Object> validateAdvertisement(Advertisement ad);

    Map<String, Object> validateBatchAdvertisements(List<Advertisement> ads);

    List<Advertisement> filterValidAdvertisements(List<Advertisement> ads);

    Map<String, Object> validateStatisticsData(Map<String, Object> statistics);

    Map<String, Object> validateHeatmapData(List<Map<String, Object>> heatmapData);

    Map<String, Object> validateChartData(List<Map<String, Object>> chartData);
}