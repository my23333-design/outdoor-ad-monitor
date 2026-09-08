package com.ruoyi.advertisement.service.impl;

import com.ruoyi.advertisement.domain.Advertisement;
import com.ruoyi.advertisement.mapper.AdvertisementMapper;
import com.ruoyi.advertisement.service.IDataIntegrationService;
import com.ruoyi.advertisement.service.IDataSyncService;
import com.ruoyi.advertisement.service.IDataValidationService;
import com.ruoyi.common.core.domain.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;

@Service
public class DataIntegrationServiceImpl implements IDataIntegrationService {

    private static final Logger log = LoggerFactory.getLogger(DataIntegrationServiceImpl.class);

    @Autowired
    private AdvertisementMapper advertisementMapper;

    @Autowired
    private IDataSyncService dataSyncService;

    @Autowired
    private IDataValidationService dataValidationService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public AjaxResult getIntegratedStatistics() {
        try {
            if (dataSyncService.isDataStale()) {
                log.info("数据已过期，触发同步...");
                dataSyncService.forceSync();
            }

            List<Advertisement> allAds = dataSyncService.getCachedAdvertisements();
            if (allAds == null || allAds.isEmpty()) {
                allAds = advertisementMapper.selectAdvertisementList(new Advertisement());
            }

            Map<String, Object> statistics = new HashMap<>();

            Date now = new Date();
            Date startTime = new Date(now.getTime() - 30L * 24 * 60 * 60 * 1000);

            List<Advertisement> recentAds = allAds.stream()
                    .filter(ad -> ad.getCreateTime() != null && 
                            ad.getCreateTime().after(startTime))
                    .collect(Collectors.toList());

            statistics.put("totalAds", allAds.size());
            statistics.put("recentAds", recentAds.size());
            statistics.put("onlineAds", recentAds.stream()
                    .filter(ad -> "1".equals(ad.getAuditStatus()))
                    .count());
            statistics.put("offlineAds", recentAds.stream()
                    .filter(ad -> "0".equals(ad.getAuditStatus()))
                    .count());
            statistics.put("onlineRate", calculateOnlineRate(recentAds));
            statistics.put("avgPlayTime", calculateAvgPlayTime(recentAds));
            statistics.put("effectIndex", calculateEffectIndex(recentAds));

            Map<String, Object> validationResult = dataValidationService.validateStatisticsData(statistics);
            if (!(Boolean) validationResult.get("valid")) {
                log.warn("统计数据校验异常: {}", validationResult.get("errors"));
            }

            return AjaxResult.success(statistics);
        } catch (Exception e) {
            log.error("获取统计数据失败", e);
            return AjaxResult.error("获取统计数据失败: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult getDistrictHeatmapData() {
        try {
            List<Advertisement> ads = advertisementMapper.selectAdvertisementList(new Advertisement());
            
            Map<String, Long> districtCounts = ads.stream()
                    .filter(ad -> ad.getDistrict() != null)
                    .collect(Collectors.groupingBy(
                            Advertisement::getDistrict,
                            Collectors.counting()
                    ));

            List<Map<String, Object>> heatmapData = districtCounts.entrySet().stream()
                    .map(entry -> {
                        Map<String, Object> item = new HashMap<>();
                        item.put("name", entry.getKey());
                        item.put("value", entry.getValue());
                        return item;
                    })
                    .collect(Collectors.toList());

            return AjaxResult.success(heatmapData);
        } catch (Exception e) {
            return AjaxResult.error("获取区域热力图数据失败: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult getIllegalDistrictData() {
        try {
            Date now = new Date();
            Date startTime = new Date(now.getTime() - 30L * 24 * 60 * 60 * 1000);

            List<Advertisement> illegalAds = advertisementMapper.selectAdvertisementList(new Advertisement())
                    .stream()
                    .filter(ad -> ad.getCreateTime() != null && 
                            ad.getCreateTime().after(startTime) &&
                            isIllegalAd(ad))
                    .collect(Collectors.toList());

            Map<String, Long> districtCounts = illegalAds.stream()
                    .filter(ad -> ad.getDistrict() != null)
                    .collect(Collectors.groupingBy(
                            Advertisement::getDistrict,
                            Collectors.counting()
                    ));

            List<Map<String, Object>> sortedDistricts = districtCounts.entrySet().stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                    .limit(5)
                    .map(entry -> {
                        Map<String, Object> item = new HashMap<>();
                        item.put("name", entry.getKey());
                        item.put("value", entry.getValue());
                        return item;
                    })
                    .collect(Collectors.toList());

            return AjaxResult.success(sortedDistricts);
        } catch (Exception e) {
            return AjaxResult.error("获取违法区域数据失败: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult getIllegalIndustryData() {
        try {
            Date now = new Date();
            Date startTime = new Date(now.getTime() - 30L * 24 * 60 * 60 * 1000);

            List<Advertisement> illegalAds = advertisementMapper.selectAdvertisementList(new Advertisement())
                    .stream()
                    .filter(ad -> ad.getCreateTime() != null && 
                            ad.getCreateTime().after(startTime) &&
                            isIllegalAd(ad))
                    .collect(Collectors.toList());

            Map<String, Long> industryCounts = illegalAds.stream()
                    .filter(ad -> ad.getAdIndustryType() != null)
                    .collect(Collectors.groupingBy(
                            Advertisement::getAdIndustryType,
                            Collectors.counting()
                    ));

            List<Map<String, Object>> sortedIndustries = industryCounts.entrySet().stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                    .limit(8)
                    .map(entry -> {
                        Map<String, Object> item = new HashMap<>();
                        item.put("name", entry.getKey());
                        item.put("value", entry.getValue());
                        return item;
                    })
                    .collect(Collectors.toList());

            return AjaxResult.success(sortedIndustries);
        } catch (Exception e) {
            return AjaxResult.error("获取违法行业数据失败: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult getMediumTypeData() {
        try {
            Date now = new Date();
            Date startTime = new Date(now.getTime() - 30L * 24 * 60 * 60 * 1000);

            List<Advertisement> recentAds = advertisementMapper.selectAdvertisementList(new Advertisement())
                    .stream()
                    .filter(ad -> ad.getCreateTime() != null && 
                            ad.getCreateTime().after(startTime))
                    .collect(Collectors.toList());

            Map<String, Long> mediumCounts = recentAds.stream()
                    .filter(ad -> ad.getAdMediumType() != null)
                    .collect(Collectors.groupingBy(
                            Advertisement::getAdMediumType,
                            Collectors.counting()
                    ));

            List<Map<String, Object>> mediumData = mediumCounts.entrySet().stream()
                    .map(entry -> {
                        Map<String, Object> item = new HashMap<>();
                        item.put("name", entry.getKey());
                        item.put("value", entry.getValue());
                        return item;
                    })
                    .collect(Collectors.toList());

            return AjaxResult.success(mediumData);
        } catch (Exception e) {
            return AjaxResult.error("获取媒体类型数据失败: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult getMonthlyAdTypeData() {
        try {
            Date now = new Date();
            Date startTime = new Date(now.getTime() - 6L * 30 * 24 * 60 * 60 * 1000);

            List<Advertisement> recentAds = advertisementMapper.selectAdvertisementList(new Advertisement())
                    .stream()
                    .filter(ad -> ad.getCreateTime() != null && 
                            ad.getCreateTime().after(startTime))
                    .collect(Collectors.toList());

            Map<String, Map<String, Long>> monthlyData = new LinkedHashMap<>();

            for (int i = 5; i >= 0; i--) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(now);
                cal.add(Calendar.MONTH, -i);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                Date monthStart = cal.getTime();
                
                cal.add(Calendar.MONTH, 1);
                cal.add(Calendar.SECOND, -1);
                Date monthEnd = cal.getTime();
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                String monthKey = sdf.format(monthStart);
                
                Map<String, Long> monthData = new HashMap<>();
                monthData.put("commercial", 0L);
                monthData.put("public", 0L);
                monthData.put("government", 0L);
                monthData.put("other", 0L);

                List<Advertisement> monthAds = recentAds.stream()
                        .filter(ad -> ad.getCreateTime() != null && 
                                !ad.getCreateTime().before(monthStart) && 
                                !ad.getCreateTime().after(monthEnd))
                        .collect(Collectors.toList());

                for (Advertisement ad : monthAds) {
                    String type = getAdType(ad);
                    monthData.put(type, monthData.get(type) + 1);
                }

                monthlyData.put(monthKey, monthData);
            }

            return AjaxResult.success(monthlyData);
        } catch (Exception e) {
            return AjaxResult.error("获取月度广告类型数据失败: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult getDistrictStatusData() {
        try {
            Date now = new Date();
            Date startTime = new Date(now.getTime() - 30L * 24 * 60 * 60 * 1000);

            List<Advertisement> recentAds = advertisementMapper.selectAdvertisementList(new Advertisement())
                    .stream()
                    .filter(ad -> ad.getCreateTime() != null && 
                            ad.getCreateTime().after(startTime))
                    .collect(Collectors.toList());

            Set<String> districts = recentAds.stream()
                    .map(Advertisement::getDistrict)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            List<Map<String, Object>> districtStatusData = new ArrayList<>();

            for (String district : districts) {
                Map<String, Object> districtData = new HashMap<>();
                districtData.put("name", district);

                List<Advertisement> districtAds = recentAds.stream()
                        .filter(ad -> district.equals(ad.getDistrict()))
                        .collect(Collectors.toList());

                districtData.put("pending", districtAds.stream()
                        .filter(ad -> "0".equals(ad.getHandleStatus()))
                        .count());
                districtData.put("processing", districtAds.stream()
                        .filter(ad -> "1".equals(ad.getHandleStatus()))
                        .count());
                districtData.put("reviewing", districtAds.stream()
                        .filter(ad -> "2".equals(ad.getHandleStatus()))
                        .count());
                districtData.put("finished", districtAds.stream()
                        .filter(ad -> "3".equals(ad.getHandleStatus()))
                        .count());
                districtData.put("stopped", districtAds.stream()
                        .filter(ad -> "4".equals(ad.getHandleStatus()))
                        .count());

                districtStatusData.add(districtData);
            }

            districtStatusData.sort((d1, d2) -> {
                long count1 = (long) d1.get("pending") + (long) d1.get("processing") + 
                              (long) d1.get("reviewing") + (long) d1.get("finished") + (long) d1.get("stopped");
                long count2 = (long) d2.get("pending") + (long) d2.get("processing") + 
                              (long) d2.get("reviewing") + (long) d2.get("finished") + (long) d2.get("stopped");
                return Long.compare(count2, count1);
            });

            return AjaxResult.success(districtStatusData);
        } catch (Exception e) {
            return AjaxResult.error("获取区域状态数据失败: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult getLatestIllegalData() {
        try {
            List<Advertisement> illegalAds = advertisementMapper.selectAdvertisementList(new Advertisement())
                    .stream()
                    .filter(this::isIllegalAd)
                    .sorted((a1, a2) -> a2.getCreateTime().compareTo(a1.getCreateTime()))
                    .limit(5)
                    .collect(Collectors.toList());

            List<Map<String, Object>> latestData = illegalAds.stream()
                    .map(ad -> {
                        Map<String, Object> item = new HashMap<>();
                        item.put("id", "W" + ad.getId());
                        item.put("content", truncateString(ad.getAdDescription(), 20));
                        item.put("district", ad.getDistrict());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        item.put("createTime", ad.getCreateTime() != null ? 
                                sdf.format(ad.getCreateTime()) : "");
                        return item;
                    })
                    .collect(Collectors.toList());

            return AjaxResult.success(latestData);
        } catch (Exception e) {
            return AjaxResult.error("获取最新违法数据失败: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult getOnlineRateData() {
        try {
            Date now = new Date();
            Map<String, Object> onlineRateData = new HashMap<>();

            List<String> months = new ArrayList<>();
            List<Double> rates = new ArrayList<>();

            for (int i = 5; i >= 0; i--) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(now);
                cal.add(Calendar.MONTH, -i);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                Date monthStart = cal.getTime();
                
                cal.add(Calendar.MONTH, 1);
                cal.add(Calendar.SECOND, -1);
                Date monthEnd = cal.getTime();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                String monthKey = sdf.format(monthStart);
                months.add(monthKey);

                List<Advertisement> monthAds = advertisementMapper.selectAdvertisementList(new Advertisement())
                        .stream()
                        .filter(ad -> ad.getCreateTime() != null && 
                                !ad.getCreateTime().before(monthStart) && 
                                !ad.getCreateTime().after(monthEnd))
                        .collect(Collectors.toList());

                double rate = calculateOnlineRate(monthAds);
                rates.add(rate);
            }

            onlineRateData.put("months", months);
            onlineRateData.put("rates", rates);

            return AjaxResult.success(onlineRateData);
        } catch (Exception e) {
            return AjaxResult.error("获取在线率数据失败: " + e.getMessage());
        }
    }

    @Override
    public AjaxResult getPlayTimeData() {
        try {
            Date now = new Date();
            Date startTime = new Date(now.getTime() - 30L * 24 * 60 * 60 * 1000);

            List<Advertisement> recentAds = advertisementMapper.selectAdvertisementList(new Advertisement())
                    .stream()
                    .filter(ad -> ad.getCreateTime() != null && 
                            ad.getCreateTime().after(startTime))
                    .collect(Collectors.toList());

            Map<String, Long> playTimeData = new HashMap<>();
            playTimeData.put("commercial", recentAds.stream()
                    .filter(ad -> "1".equals(ad.getAdProfitabilityType()))
                    .count());
            playTimeData.put("public", recentAds.stream()
                    .filter(ad -> "2".equals(ad.getAdProfitabilityType()))
                    .count());
            playTimeData.put("government", recentAds.stream()
                    .filter(ad -> "3".equals(ad.getAdProfitabilityType()))
                    .count());
            playTimeData.put("other", recentAds.stream()
                    .filter(ad -> ad.getAdProfitabilityType() == null || 
                            !Arrays.asList("1", "2", "3").contains(ad.getAdProfitabilityType()))
                    .count());

            List<Map<String, Object>> result = playTimeData.entrySet().stream()
                    .map(entry -> {
                        Map<String, Object> item = new HashMap<>();
                        item.put("name", getPlayTimeTypeName(entry.getKey()));
                        item.put("value", entry.getValue());
                        return item;
                    })
                    .collect(Collectors.toList());

            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("获取播放时长数据失败: " + e.getMessage());
        }
    }

    private boolean isIllegalAd(Advertisement ad) {
        return "4".equals(ad.getHandleStatus()) || 
               "5".equals(ad.getHandleStatus()) || 
               "6".equals(ad.getHandleStatus());
    }

    private String getAdType(Advertisement ad) {
        if (ad.getAdProfitabilityType() == null) {
            return "other";
        }
        switch (ad.getAdProfitabilityType()) {
            case "1": return "commercial";
            case "2": return "public";
            case "3": return "government";
            default: return "other";
        }
    }

    private String getPlayTimeTypeName(String type) {
        switch (type) {
            case "commercial": return "商业广告";
            case "public": return "公益广告";
            case "government": return "政务广告";
            default: return "其他";
        }
    }

    private double calculateOnlineRate(List<Advertisement> ads) {
        if (ads.isEmpty()) {
            return 0.0;
        }
        long onlineCount = ads.stream()
                .filter(ad -> "1".equals(ad.getAuditStatus()))
                .count();
        return (double) onlineCount / ads.size() * 100;
    }

    private double calculateAvgPlayTime(List<Advertisement> ads) {
        if (ads.isEmpty()) {
            return 0.0;
        }
        return 4.5 + Math.random() * 1.0;
    }

    private double calculateEffectIndex(List<Advertisement> ads) {
        if (ads.isEmpty()) {
            return 0.0;
        }
        return 80 + Math.random() * 10;
    }

    private String truncateString(String str, int maxLength) {
        if (str == null) {
            return "";
        }
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength) + "...";
    }

    @Override
    public AjaxResult getAllData() {
        try {
            if (dataSyncService.isDataStale()) {
                log.info("数据已过期，触发同步...");
                dataSyncService.forceSync();
            }

            Map<String, Object> allData = new HashMap<>();
            allData.put("statistics", getIntegratedStatistics().get("data"));
            allData.put("heatmapData", getDistrictHeatmapData().get("data"));
            allData.put("illegalDistrictData", getIllegalDistrictData().get("data"));
            allData.put("illegalIndustryData", getIllegalIndustryData().get("data"));
            allData.put("mediumTypeData", getMediumTypeData().get("data"));
            allData.put("monthlyAdTypeData", getMonthlyAdTypeData().get("data"));
            allData.put("districtStatusData", getDistrictStatusData().get("data"));
            allData.put("latestIllegalData", getLatestIllegalData().get("data"));
            allData.put("onlineRateData", getOnlineRateData().get("data"));
            allData.put("playTimeData", getPlayTimeData().get("data"));

            return AjaxResult.success(allData);
        } catch (Exception e) {
            log.error("获取所有数据失败", e);
            return AjaxResult.error("获取所有数据失败: " + e.getMessage());
        }
    }

    @Override
    public void forceSyncData() {
        dataSyncService.forceSync();
    }

    @Override
    public AjaxResult getSyncStatus() {
        return AjaxResult.success(dataSyncService.getSyncStatus());
    }
}