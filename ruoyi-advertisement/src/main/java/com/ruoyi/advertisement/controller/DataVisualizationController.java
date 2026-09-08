package com.ruoyi.advertisement.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 广告投放数据可视化Controller
 */
@RestController
@RequestMapping("/advertisement/dataVisualization")
public class DataVisualizationController extends BaseController {

    /**
     * 获取统计数据
     * @return 统计数据
     */
    @GetMapping("/getStatistics")
    public AjaxResult getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalAds", 128);
        statistics.put("onlineRate", 92.5);
        statistics.put("avgPlayTime", 4.8);
        statistics.put("effectIndex", 85);
        return success(statistics);
    }

    /**
     * 获取投放效果热力图数据
     * @return 热力图数据
     */
    @GetMapping("/getHeatmapData")
    public AjaxResult getHeatmapData() {
        List<List<Object>> data = new ArrayList<>();
        // 模拟数据
        int[][] values = {
            {80, 65, 75, 90},
            {70, 85, 60, 75},
            {90, 70, 80, 85},
            {65, 80, 75, 90},
            {85, 75, 90, 80},
            {70, 80, 75, 85},
            {80, 90, 85, 95},
            {90, 85, 95, 90},
            {85, 90, 80, 85},
            {75, 80, 85, 90},
            {80, 75, 90, 85},
            {85, 90, 80, 95}
        };
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                List<Object> item = new ArrayList<>();
                item.add(i);
                item.add(j);
                item.add(values[i][j]);
                data.add(item);
            }
        }
        return success(data);
    }

    /**
     * 获取设备在线率趋势图数据
     * @return 在线率趋势数据
     */
    @GetMapping("/getOnlineRateData")
    public AjaxResult getOnlineRateData() {
        Map<String, Object> data = new HashMap<>();
        data.put("months", new String[]{"1月", "2月", "3月", "4月", "5月", "6月"});
        data.put("rates", new double[]{90.5, 91.2, 92.8, 93.5, 92.1, 92.5});
        return success(data);
    }

    /**
     * 获取广告播放时长统计数据
     * @return 播放时长统计数据
     */
    @GetMapping("/getPlayTimeData")
    public AjaxResult getPlayTimeData() {
        List<Map<String, Object>> data = new ArrayList<>();
        Map<String, Object> item1 = new HashMap<>();
        item1.put("value", 35);
        item1.put("name", "商业广告");
        data.add(item1);
        Map<String, Object> item2 = new HashMap<>();
        item2.put("value", 25);
        item2.put("name", "公益广告");
        data.add(item2);
        Map<String, Object> item3 = new HashMap<>();
        item3.put("value", 20);
        item3.put("name", "政务广告");
        data.add(item3);
        Map<String, Object> item4 = new HashMap<>();
        item4.put("value", 20);
        item4.put("name", "其他");
        data.add(item4);
        return success(data);
    }
}
