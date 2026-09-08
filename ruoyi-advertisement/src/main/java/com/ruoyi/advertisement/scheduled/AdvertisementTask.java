package com.ruoyi.advertisement.scheduled;

import com.ruoyi.advertisement.domain.Advertisement;
import com.ruoyi.advertisement.mapper.AdvertisementMapper;
import com.ruoyi.advertisement.service.impl.AdvertisementServiceImpl;
import com.ruoyi.common.utils.spring.SpringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 复审定时任务
 */
@Component
public class AdvertisementTask {

    /**
     * 自动复审
     * 每天凌晨1点执行
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void automatedReview() {
        //获取广告业务层
        AdvertisementServiceImpl advertisementService = SpringUtils.getBean("advertisementServiceImpl");
        //调用自动复审的方法
        advertisementService.automatedReview();
    }
}