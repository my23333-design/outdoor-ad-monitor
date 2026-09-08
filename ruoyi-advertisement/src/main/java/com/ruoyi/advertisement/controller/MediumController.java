package com.ruoyi.advertisement.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.core.domain.AjaxResult;

/**
 * 媒体类型接口
 * 用于微信小程序操作Redis数据库中的媒体类型数据
 */
@RestController
@RequestMapping("/api/advertisement/medium")
public class MediumController
{
    @Autowired
    private RedisCache redisCache;

    /**
     * Redis中媒体类型的Key前缀
     */
    private static final String MEDIUM_KEY_PREFIX = "medium:";

    /**
     * 从Redis中获取媒体类型列表
     * 
     * @return 媒体类型列表
     */
    @GetMapping("/list")
    public AjaxResult getMediumTypeList()
    {
        List<String> mediumList = new ArrayList<>();
        
        Collection<String> keys = redisCache.keys(MEDIUM_KEY_PREFIX + "*");
        if (keys != null && !keys.isEmpty())
        {
            for (String key : keys)
            {
                Object valueObj = redisCache.getCacheObject(key);
                if (valueObj != null)
                {
                    String value = valueObj.toString();
                    if (!value.isEmpty())
                    {
                        List<String> items = parseRedisList(value);
                        for (String item : items)
                        {
                            if (!mediumList.contains(item))
                            {
                                mediumList.add(item);
                            }
                        }
                    }
                }
            }
        }
        System.out.println("媒体类型返回: " + mediumList.size() + " 条, 数据: " + mediumList);
        return AjaxResult.success(mediumList);
    }

    /**
     * 解析 Redis 字符串数组（可能带外层引号和转义，例如 "[\"电子屏\",\"户外广告\"]"
     */
    private List<String> parseRedisList(String raw)
    {
        List<String> list = new ArrayList<>();
        if (raw == null || raw.isEmpty()) return list;

        String value = raw.trim();
        if (value.startsWith("\"") && value.endsWith("\"") && value.length() >= 2)
        {
            value = value.substring(1, value.length() - 1);
        }
        value = value.replace("\\\"", "\"").replace("\\\\", "\\");

        try
        {
            JSONArray arr = JSON.parseArray(value);
            for (int i = 0; i < arr.size(); i++)
            {
                String item = arr.getString(i);
                if (item != null && !item.trim().isEmpty() && !list.contains(item.trim()))
                {
                    list.add(item.trim());
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("解析Redis媒体类型数组失败: " + e.getMessage());
            value = value.replace("[", "").replace("]", "").replace("\"", "");
            for (String s : value.split(","))
            {
                String item = s.trim();
                if (!item.isEmpty() && !list.contains(item))
                {
                    list.add(item);
                }
            }
        }
        return list;
    }

    /**
     * 检测媒体列表中是否存在该媒体类型
     * 
     * @param medium 媒体类型名称
     * @return 是否存在
     */
    @GetMapping("/check")
    public AjaxResult checkMediumType(@RequestParam String medium)
    {
        List<String> mediumList = getMediumListData();
        boolean exists = mediumList.contains(medium);
        return AjaxResult.success(exists);
    }

    /**
     * 获取媒体列表数据（内部方法）
     */
    private List<String> getMediumListData()
    {
        List<String> mediumList = new ArrayList<>();
        Collection<String> keys = redisCache.keys(MEDIUM_KEY_PREFIX + "*");
        if (keys != null && !keys.isEmpty())
        {
            for (String key : keys)
            {
                Object valueObj = redisCache.getCacheObject(key);
                if (valueObj != null)
                {
                    String value = valueObj.toString();
                    if (!value.isEmpty())
                    {
                        try
                        {
                            JSONArray jsonArray = JSON.parseArray(value);
                            for (int i = 0; i < jsonArray.size(); i++)
                            {
                                String medium = jsonArray.getString(i);
                                if (!mediumList.contains(medium))
                                {
                                    mediumList.add(medium);
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            if (!mediumList.contains(value))
                            {
                                mediumList.add(value);
                            }
                        }
                    }
                }
            }
        }
        return mediumList;
    }

    /**
     * 新增媒体类型到Redis中
     * 
     * @param medium 媒体类型名称
     * @return 操作结果
     */
    @PostMapping
    public AjaxResult addMediumType(@RequestParam String medium)
    {
        if (medium == null || medium.trim().isEmpty())
        {
            return AjaxResult.error("媒体类型名称不能为空");
        }
        
        List<String> mediumList = getMediumListData();
        if (!mediumList.contains(medium))
        {
            mediumList.add(medium);
            String jsonValue = JSON.toJSONString(mediumList);
            redisCache.setCacheObject(MEDIUM_KEY_PREFIX + "list", jsonValue);
        }
        return AjaxResult.success("添加成功");
    }
}