package com.ruoyi.advertisement.service.impl;

import com.ruoyi.advertisement.domain.Advertisement;
import com.ruoyi.advertisement.mapper.AdvertisementMapper;
import com.ruoyi.advertisement.service.IDataSyncService;
import com.ruoyi.advertisement.service.IDataValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DataSyncServiceImpl implements IDataSyncService {

    private static final Logger log = LoggerFactory.getLogger(DataSyncServiceImpl.class);

    @Autowired
    private AdvertisementMapper advertisementMapper;

    @Autowired
    private IDataValidationService dataValidationService;

    private final Map<String, Object> dataCache = new ConcurrentHashMap<>();
    private LocalDateTime lastSyncTime = null;
    private final Map<String, LocalDateTime> lastModifiedTimes = new ConcurrentHashMap<>();

    @Override
    public boolean syncDataFromBusinessSystem() {
        try {
            log.info("开始同步业务系统数据...");
            
            List<Advertisement> allAds = advertisementMapper.selectAdvertisementList(new Advertisement());
            
            Map<String, Object> validationResult = dataValidationService.validateBatchAdvertisements(allAds);
            
            if (!(Boolean) validationResult.get("valid")) {
                log.warn("数据校验发现异常: {}", validationResult.get("allErrors"));
            }

            List<Advertisement> validAds = dataValidationService.filterValidAdvertisements(allAds);
            
            dataCache.put("allAdvertisements", validAds);
            dataCache.put("lastSyncTime", LocalDateTime.now());
            
            lastSyncTime = LocalDateTime.now();
            
            log.info("数据同步完成，共同步{}条有效数据", validAds.size());
            return true;
            
        } catch (Exception e) {
            log.error("数据同步失败", e);
            return false;
        }
    }

    @Override
    @Scheduled(fixedRate = 60000)
    public void autoSyncData() {
        try {
            syncDataFromBusinessSystem();
            updateLastModifiedTimes();
        } catch (Exception e) {
            log.error("自动数据同步失败", e);
        }
    }

    @Override
    public boolean syncIncrementalData() {
        try {
            log.info("开始同步增量数据...");
            
            LocalDateTime lastSync = getLastSyncTime();
            if (lastSync == null) {
                lastSync = LocalDateTime.now().minusDays(1);
            }

            // 将LocalDateTime转换为Date
            Date lastSyncDate = java.sql.Timestamp.valueOf(lastSync);

            Advertisement queryCondition = new Advertisement();
            List<Advertisement> allAds = advertisementMapper.selectAdvertisementList(queryCondition);
            
            List<Advertisement> incrementalAds = new ArrayList<>();
            for (Advertisement ad : allAds) {
                if (ad.getUpdateTime() != null && ad.getUpdateTime().after(lastSyncDate)) {
                    incrementalAds.add(ad);
                }
            }

            if (!incrementalAds.isEmpty()) {
                List<Advertisement> cachedAds = (List<Advertisement>) dataCache.get("allAdvertisements");
                if (cachedAds != null) {
                    cachedAds.addAll(incrementalAds);
                    dataCache.put("allAdvertisements", cachedAds);
                }
                
                log.info("增量数据同步完成，共同步{}条新数据", incrementalAds.size());
            } else {
                log.info("没有新的增量数据需要同步");
            }

            lastSyncTime = LocalDateTime.now();
            return true;
            
        } catch (Exception e) {
            log.error("增量数据同步失败", e);
            return false;
        }
    }

    @Override
    public Map<String, Object> getCachedData(String key) {
        return (Map<String, Object>) dataCache.get(key);
    }

    @Override
    public List<Advertisement> getCachedAdvertisements() {
        return (List<Advertisement>) dataCache.get("allAdvertisements");
    }

    @Override
    public LocalDateTime getLastSyncTime() {
        return lastSyncTime;
    }

    @Override
    public boolean isDataStale() {
        if (lastSyncTime == null) {
            return true;
        }
        
        LocalDateTime now = LocalDateTime.now();
        return lastSyncTime.plusMinutes(5).isBefore(now);
    }

    @Override
    public void forceSync() {
        log.info("强制同步数据...");
        syncDataFromBusinessSystem();
    }

    @Override
    public Map<String, Object> getSyncStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("lastSyncTime", lastSyncTime);
        status.put("isStale", isDataStale());
        status.put("cacheSize", dataCache.size());
        status.put("advertisementCount", 
            dataCache.containsKey("allAdvertisements") ? 
            ((List<?>) dataCache.get("allAdvertisements")).size() : 0);
        return status;
    }

    @Override
    public void clearCache() {
        dataCache.clear();
        lastSyncTime = null;
        log.info("数据缓存已清空");
    }

    private void updateLastModifiedTimes() {
        try {
            List<Advertisement> ads = advertisementMapper.selectAdvertisementList(new Advertisement());
            
            Map<String, Date> districtTimes = new HashMap<>();
            Map<String, Date> industryTimes = new HashMap<>();
            
            for (Advertisement ad : ads) {
                if (ad.getDistrict() != null && ad.getUpdateTime() != null) {
                    Date current = districtTimes.get(ad.getDistrict());
                    if (current == null || ad.getUpdateTime().after(current)) {
                        districtTimes.put(ad.getDistrict(), ad.getUpdateTime());
                    }
                }
                
                if (ad.getAdIndustryType() != null && ad.getUpdateTime() != null) {
                    Date current = industryTimes.get(ad.getAdIndustryType());
                    if (current == null || ad.getUpdateTime().after(current)) {
                        industryTimes.put(ad.getAdIndustryType(), ad.getUpdateTime());
                    }
                }
            }
            
            // 清空并重新填充lastModifiedTimes，使用Date类型
            lastModifiedTimes.clear();
            // 这里需要将Date转换为LocalDateTime，因为lastModifiedTimes的类型是Map<String, LocalDateTime>
            districtTimes.forEach((key, value) -> {
                lastModifiedTimes.put(key, value.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
            });
            industryTimes.forEach((key, value) -> {
                lastModifiedTimes.put(key, value.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
            });
            
        } catch (Exception e) {
            log.error("更新最后修改时间失败", e);
        }
    }
}