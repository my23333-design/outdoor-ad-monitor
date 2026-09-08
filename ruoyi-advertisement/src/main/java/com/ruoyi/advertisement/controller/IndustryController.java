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
 * 行业类型接口
 * 用于微信小程序操作Redis数据库中的行业类型数据
 */
@RestController
@RequestMapping("/api/advertisement/industry")
public class IndustryController
{
    @Autowired
    private RedisCache redisCache;

    /**
     * Redis中行业类型的Key前缀
     */
    private static final String INDUSTRY_KEY_PREFIX = "industry:";

    /**
     * 从Redis中获取行业类型列表
     * 
     * @return 行业类型列表
     */
    @GetMapping("/list")
    public AjaxResult getIndustryList()
    {
        List<String> industryList = new ArrayList<>();
        
        Collection<String> keys = redisCache.keys(INDUSTRY_KEY_PREFIX + "*");
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
                            if (!industryList.contains(item))
                            {
                                industryList.add(item);
                            }
                        }
                    }
                }
            }
        }
        System.out.println("行业分类返回: " + industryList.size() + " 条, 数据: " + industryList);
        return AjaxResult.success(industryList);
    }

    /**
     * 解析 Redis 字符串数组（可能带外层引号和转义，例如 "[\"医疗\",\"小说\"]"）
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
            System.out.println("解析Redis行业数组失败: " + e.getMessage());
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
     * 检测行业列表中是否存在该行业
     * 
     * @param industry 行业名称
     * @return 是否存在
     */
    @GetMapping("/check")
    public AjaxResult checkIndustry(@RequestParam String industry)
    {
        List<String> industryList = getIndustryListData();
        boolean exists = industryList.contains(industry);
        return AjaxResult.success(exists);
    }

    /**
     * 获取行业列表数据（内部方法）
     */
    private List<String> getIndustryListData()
    {
        List<String> industryList = new ArrayList<>();
        Collection<String> keys = redisCache.keys(INDUSTRY_KEY_PREFIX + "*");
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
                                String industry = jsonArray.getString(i);
                                if (!industryList.contains(industry))
                                {
                                    industryList.add(industry);
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            if (!industryList.contains(value))
                            {
                                industryList.add(value);
                            }
                        }
                    }
                }
            }
        }
        return industryList;
    }

    /**
     * 新增行业类型到Redis中
     * 
     * @param industry 行业名称
     * @return 操作结果
     */
    @PostMapping
    public AjaxResult addIndustry(@RequestParam String industry)
    {
        if (industry == null || industry.trim().isEmpty())
        {
            return AjaxResult.error("行业名称不能为空");
        }
        
        List<String> industryList = getIndustryListData();
        if (!industryList.contains(industry))
        {
            industryList.add(industry);
            String jsonValue = JSON.toJSONString(industryList);
            redisCache.setCacheObject(INDUSTRY_KEY_PREFIX + "list", jsonValue);
        }
        return AjaxResult.success("添加成功");
    }
}