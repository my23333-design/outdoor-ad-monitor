package com.ruoyi.advertisement.service.impl;

import com.ruoyi.advertisement.domain.Advertisement;
import com.ruoyi.advertisement.service.IDataValidationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

@Service
public class DataValidationServiceImpl implements IDataValidationService {

    @Override
    public Map<String, Object> validateAdvertisement(Advertisement ad) {
        Map<String, Object> validationResult = new HashMap<>();
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        if (ad == null) {
            errors.add("广告数据为空");
            validationResult.put("valid", false);
            validationResult.put("errors", errors);
            validationResult.put("warnings", warnings);
            return validationResult;
        }

        if (ad.getAdDescription() == null || ad.getAdDescription().trim().isEmpty()) {
            warnings.add("广告描述为空");
        }

        if (ad.getDistrict() == null || ad.getDistrict().trim().isEmpty()) {
            errors.add("所属区域不能为空");
        }

        if (ad.getAdIndustryType() == null || ad.getAdIndustryType().trim().isEmpty()) {
            warnings.add("广告行业类型为空");
        }

        if (ad.getAdMediumType() == null || ad.getAdMediumType().trim().isEmpty()) {
            warnings.add("广告媒体类型为空");
        }

        if (ad.getAuditStatus() == null || ad.getAuditStatus().trim().isEmpty()) {
            errors.add("审核状态不能为空");
        } else if (!isValidAuditStatus(ad.getAuditStatus())) {
            errors.add("审核状态值无效");
        }

        if (ad.getHandleStatus() == null || ad.getHandleStatus().trim().isEmpty()) {
            warnings.add("处理状态为空");
        } else if (!isValidHandleStatus(ad.getHandleStatus())) {
            warnings.add("处理状态值可能无效");
        }

        if (ad.getCreateTime() == null) {
            errors.add("创建时间不能为空");
        } else if (ad.getCreateTime().after(new Date())) {
            errors.add("创建时间不能晚于当前时间");
        }

        if (ad.getSurveyTime() != null && ad.getCreateTime() != null) {
            if (ad.getSurveyTime().before(ad.getCreateTime())) {
                warnings.add("调查时间早于创建时间");
            }
        }

        validationResult.put("valid", errors.isEmpty());
        validationResult.put("errors", errors);
        validationResult.put("warnings", warnings);

        return validationResult;
    }

    @Override
    public Map<String, Object> validateBatchAdvertisements(List<Advertisement> ads) {
        Map<String, Object> validationResult = new HashMap<>();
        List<Map<String, Object>> validationResults = new ArrayList<>();
        int validCount = 0;
        int invalidCount = 0;
        List<String> allErrors = new ArrayList<>();
        List<String> allWarnings = new ArrayList<>();

        for (Advertisement ad : ads) {
            Map<String, Object> result = validateAdvertisement(ad);
            validationResults.add(result);

            if ((Boolean) result.get("valid")) {
                validCount++;
            } else {
                invalidCount++;
            }

            List<String> errors = (List<String>) result.get("errors");
            List<String> warnings = (List<String>) result.get("warnings");

            allErrors.addAll(errors);
            allWarnings.addAll(warnings);
        }

        validationResult.put("totalCount", ads.size());
        validationResult.put("validCount", validCount);
        validationResult.put("invalidCount", invalidCount);
        validationResult.put("validationResults", validationResults);
        validationResult.put("allErrors", allErrors);
        validationResult.put("allWarnings", allWarnings);

        return validationResult;
    }

    @Override
    public List<Advertisement> filterValidAdvertisements(List<Advertisement> ads) {
        List<Advertisement> validAds = new ArrayList<>();

        for (Advertisement ad : ads) {
            Map<String, Object> result = validateAdvertisement(ad);
            if ((Boolean) result.get("valid")) {
                validAds.add(ad);
            }
        }

        return validAds;
    }

    @Override
    public Map<String, Object> validateStatisticsData(Map<String, Object> statistics) {
        Map<String, Object> validationResult = new HashMap<>();
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        if (statistics == null) {
            errors.add("统计数据为空");
            validationResult.put("valid", false);
            validationResult.put("errors", errors);
            validationResult.put("warnings", warnings);
            return validationResult;
        }

        try {
            Object totalAds = statistics.get("totalAds");
            if (totalAds == null) {
                warnings.add("总广告数量为空");
            } else if (totalAds instanceof Number) {
                long count = ((Number) totalAds).longValue();
                if (count < 0) {
                    errors.add("总广告数量不能为负数");
                }
            }

            Object onlineRate = statistics.get("onlineRate");
            if (onlineRate == null) {
                warnings.add("在线率为空");
            } else if (onlineRate instanceof Number) {
                double rate = ((Number) onlineRate).doubleValue();
                if (rate < 0 || rate > 100) {
                    errors.add("在线率必须在0-100之间");
                }
            }

            Object avgPlayTime = statistics.get("avgPlayTime");
            if (avgPlayTime == null) {
                warnings.add("平均播放时长为空");
            } else if (avgPlayTime instanceof Number) {
                double time = ((Number) avgPlayTime).doubleValue();
                if (time < 0) {
                    errors.add("平均播放时长不能为负数");
                }
            }

            Object effectIndex = statistics.get("effectIndex");
            if (effectIndex == null) {
                warnings.add("效果指数为空");
            } else if (effectIndex instanceof Number) {
                double index = ((Number) effectIndex).doubleValue();
                if (index < 0 || index > 100) {
                    errors.add("效果指数必须在0-100之间");
                }
            }

        } catch (Exception e) {
            errors.add("统计数据格式验证失败: " + e.getMessage());
        }

        validationResult.put("valid", errors.isEmpty());
        validationResult.put("errors", errors);
        validationResult.put("warnings", warnings);

        return validationResult;
    }

    @Override
    public Map<String, Object> validateHeatmapData(List<Map<String, Object>> heatmapData) {
        Map<String, Object> validationResult = new HashMap<>();
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        if (heatmapData == null || heatmapData.isEmpty()) {
            warnings.add("热力图数据为空");
            validationResult.put("valid", true);
            validationResult.put("errors", errors);
            validationResult.put("warnings", warnings);
            return validationResult;
        }

        for (int i = 0; i < heatmapData.size(); i++) {
            Map<String, Object> item = heatmapData.get(i);

            if (!item.containsKey("name") || item.get("name") == null) {
                errors.add("热力图数据第" + (i + 1) + "项缺少区域名称");
            }

            if (!item.containsKey("value") || item.get("value") == null) {
                errors.add("热力图数据第" + (i + 1) + "项缺少数值");
            } else if (item.get("value") instanceof Number) {
                double value = ((Number) item.get("value")).doubleValue();
                if (value < 0) {
                    errors.add("热力图数据第" + (i + 1) + "项数值不能为负数");
                }
            }
        }

        validationResult.put("valid", errors.isEmpty());
        validationResult.put("errors", errors);
        validationResult.put("warnings", warnings);

        return validationResult;
    }

    @Override
    public Map<String, Object> validateChartData(List<Map<String, Object>> chartData) {
        Map<String, Object> validationResult = new HashMap<>();
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        if (chartData == null || chartData.isEmpty()) {
            warnings.add("图表数据为空");
            validationResult.put("valid", true);
            validationResult.put("errors", errors);
            validationResult.put("warnings", warnings);
            return validationResult;
        }

        for (int i = 0; i < chartData.size(); i++) {
            Map<String, Object> item = chartData.get(i);

            if (!item.containsKey("name") || item.get("name") == null) {
                errors.add("图表数据第" + (i + 1) + "项缺少名称");
            }

            if (!item.containsKey("value") || item.get("value") == null) {
                errors.add("图表数据第" + (i + 1) + "项缺少数值");
            } else if (item.get("value") instanceof Number) {
                double value = ((Number) item.get("value")).doubleValue();
                if (value < 0) {
                    errors.add("图表数据第" + (i + 1) + "项数值不能为负数");
                }
            }
        }

        validationResult.put("valid", errors.isEmpty());
        validationResult.put("errors", errors);
        validationResult.put("warnings", warnings);

        return validationResult;
    }

    private boolean isValidAuditStatus(String status) {
        return "0".equals(status) || "1".equals(status) || "2".equals(status) || 
               "3".equals(status) || "4".equals(status) || "5".equals(status) || 
               "6".equals(status) || "7".equals(status) || "8".equals(status);
    }

    private boolean isValidHandleStatus(String status) {
        return "0".equals(status) || "1".equals(status) || "2".equals(status) || 
               "3".equals(status) || "4".equals(status);
    }
}