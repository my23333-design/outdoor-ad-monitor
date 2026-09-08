package com.ruoyi.advertisement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import com.ruoyi.advertisement.domain.Advertisement;

public interface IDataSyncService {

    boolean syncDataFromBusinessSystem();

    void autoSyncData();

    boolean syncIncrementalData();

    Map<String, Object> getCachedData(String key);

    List<Advertisement> getCachedAdvertisements();

    LocalDateTime getLastSyncTime();

    boolean isDataStale();

    void forceSync();

    Map<String, Object> getSyncStatus();

    void clearCache();
}