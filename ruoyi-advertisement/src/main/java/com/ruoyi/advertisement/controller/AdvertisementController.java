package com.ruoyi.advertisement.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.advertisement.domain.Advertisement;
import com.ruoyi.advertisement.service.IAdvertisementService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.common.utils.map.MapUtils;
import com.ruoyi.common.utils.map.AddressComponent;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.ruoyi.common.utils.DateUtils;

/**
 * 广告列表Controller
 * 
 * @author ruoyi
 * @date 2026-03-18
 */
@RestController
@RequestMapping({"/advertisement/advertisement", "/api/advertisement/advertisement"})
public class AdvertisementController extends BaseController
{
    @Autowired
    private IAdvertisementService advertisementService;
    
    @Autowired
    private ServerConfig serverConfig;
    
    @Autowired
    private MapUtils mapUtils;
    
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 查询广告列表列表
     */
    @PreAuthorize("@ss.hasPermi('advertisement:advertisement:list')")
    @GetMapping("/list")
    public TableDataInfo list(Advertisement advertisement)
    {
        List<Advertisement> list = advertisementService.selectAdvertisementList(advertisement);
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(list.size());
        return rspData;
    }

    /**
     * 导出广告列表列表
     */
    @PreAuthorize("@ss.hasPermi('advertisement:advertisement:export')")
    @Log(title = "广告列表", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Advertisement advertisement)
    {
        List<Advertisement> list = advertisementService.selectAdvertisementList(advertisement);
        ExcelUtil<Advertisement> util = new ExcelUtil<Advertisement>(Advertisement.class);
        util.exportExcel(response, list, "广告列表数据");
    }

    /**
     * 获取广告列表详细信息
     */
    @PreAuthorize("@ss.hasPermi('advertisement:advertisement:query')")
    @GetMapping(value = "/{id:\\d+}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(advertisementService.selectAdvertisementById(id));
    }

    /**
     * 新增广告列表
     */
    @Log(title = "广告列表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Advertisement advertisement)
    {
        return toAjax(advertisementService.insertAdvertisement(advertisement));
    }
    
    /**
     * 小程序提交广告数据
     */
    @PostMapping("/submit")
    public AjaxResult submitFromMiniProgram(@RequestBody Advertisement advertisement)
    {
        System.out.println("=== 小程序提交广告数据 ===");
        System.out.println("原始数据: " + advertisement.toString());
        
        advertisement.setAuditStatus("0");
        
        if (advertisement.getViolationType() == null || advertisement.getViolationType().isEmpty()) {
            advertisement.setViolationType("未分类");
        }
        if (advertisement.getCheckStatus() == null || advertisement.getCheckStatus().isEmpty()) {
            advertisement.setCheckStatus("0");
        }
        if (advertisement.getHandleStatus() == null || advertisement.getHandleStatus().isEmpty()) {
            advertisement.setHandleStatus("0");
        }
        if (advertisement.getIsDeleted() == null) {
            advertisement.setIsDeleted(0);
        }
        if (advertisement.getSurveyTime() == null) {
            advertisement.setSurveyTime(DateUtils.getNowDate());
        }
        if (advertisement.getSurveyor() == null || advertisement.getSurveyor().isEmpty()) {
            advertisement.setSurveyor("小程序用户");
        }
        
        // 规范化图片路径：将JSON数组格式的URL转为逗号分隔的相对路径
        String normalizedAdImages = normalizeImagePath(advertisement.getAdImages());
        advertisement.setAdImages(normalizedAdImages);
        
        AddressComponent addressComponent = mapUtils.findAddressByLngLat(advertisement.getLongitude(), advertisement.getLatitude());
        if (addressComponent != null) {
            if (advertisement.getProvince() == null || advertisement.getProvince().isEmpty()) {
                advertisement.setProvince(addressComponent.getProvince());
            }
            if (advertisement.getCity() == null || advertisement.getCity().isEmpty()) {
                advertisement.setCity(addressComponent.getCity());
            }
            if (advertisement.getDistrict() == null || advertisement.getDistrict().isEmpty()) {
                advertisement.setDistrict(addressComponent.getDistrict());
            }
            if (advertisement.getStreet() == null || advertisement.getStreet().isEmpty()) {
                advertisement.setStreet(addressComponent.getTownship());
            }
        }
        
        int result = advertisementService.insertAdvertisement(advertisement);
        System.out.println("插入结果: " + result);
        return toAjax(result);
    }

    /**
     * 修改广告列表
     */
    @PreAuthorize("@ss.hasPermi('advertisement:advertisement:edit')")
    @Log(title = "广告列表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Advertisement advertisement)
    {
        return toAjax(advertisementService.updateAdvertisement(advertisement));
    }

    /**
     * 删除广告列表
     */
    @PreAuthorize("@ss.hasPermi('advertisement:advertisement:remove')")
    @Log(title = "广告列表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids:[\\d,]+}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(advertisementService.deleteAdvertisementByIds(ids));
    }

    /**
     * 批量重置广告审核状态
     */
    @PreAuthorize("@ss.hasPermi('advertisement:advertisement:edit')")
    @Log(title = "广告列表", businessType = BusinessType.UPDATE)
    @PostMapping("/resetAll")
    public AjaxResult resetAllAdvertisements()
    {
        int rows = advertisementService.resetAllAdvertisementStatus();
        return toAjax(rows);
    }

    /**
     * 上传广告图片
     */
    @PostMapping("/upload")
    public AjaxResult uploadFile(MultipartFile file) throws Exception
    {
        try
        {
            String filePath = RuoYiConfig.getUploadPath();
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 批量上传广告图片
     */
    @PostMapping("/uploads")
    public AjaxResult uploadFiles(List<MultipartFile> files) throws Exception
    {
        try
        {
            String filePath = RuoYiConfig.getUploadPath();
            List<String> urls = new ArrayList<String>();
            for (MultipartFile file : files)
            {
                String fileName = FileUploadUtils.upload(filePath, file);
                String url = serverConfig.getUrl() + fileName;
                urls.add(url);
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("urls", urls);
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 小程序获取叫停广告数据
     */
    @GetMapping("/stopped/list")
    public AjaxResult getStoppedList()
    {
        System.out.println("=== 小程序请求叫停广告列表 ===");
        
        Advertisement query = new Advertisement();
        List<Advertisement> allList = advertisementService.selectAdvertisementList(query);
        
        List<Advertisement> stoppedList = new ArrayList<>();
        for (Advertisement ad : allList) {
            String auditStatus = ad.getAuditStatus();
            if ("3".equals(auditStatus) || "6".equals(auditStatus) || "8".equals(auditStatus)) {
                stoppedList.add(ad);
            }
        }
        
        System.out.println("返回叫停广告数量: " + stoppedList.size());
        return success(stoppedList);
    }

    /**
     * 小程序提交处理结果
     */
    @PostMapping("/processResult/submit")
    public AjaxResult submitProcessResult(@RequestBody Advertisement advertisement)
    {
        System.out.println("=== 小程序提交处理结果 ===");
        System.out.println("广告ID: " + advertisement.getId());
        System.out.println("处理结果: " + advertisement.getProcessResult());
        System.out.println("处理后图片原始路径: " + advertisement.getAfterImages());
        
        // 规范化处理后图片路径：将JSON数组格式的URL转为逗号分隔的相对路径
        String normalizedAfterImages = normalizeImagePath(advertisement.getAfterImages());
        advertisement.setAfterImages(normalizedAfterImages);
        System.out.println("规范化后的处理后图片路径: " + normalizedAfterImages);
        
        int result = advertisementService.updateAdvertisement(advertisement);
        return toAjax(result);
    }

    /**
     * 获取已处理的广告列表
     */
    @GetMapping("/processed/list")
    public AjaxResult getProcessedList()
    {
        System.out.println("=== 请求已处理广告列表 ===");
        
        Advertisement query = new Advertisement();
        List<Advertisement> allList = advertisementService.selectAdvertisementList(query);
        
        List<Advertisement> processedList = new ArrayList<>();
        for (Advertisement ad : allList) {
            if (ad.getProcessResult() != null && !ad.getProcessResult().isEmpty()) {
                processedList.add(ad);
            }
        }
        
        System.out.println("返回已处理广告数量: " + processedList.size());
        return success(processedList);
    }
    
    /**
     * 清理无效的处理后图片路径
     */
    @PostMapping("/cleanInvalidImages")
    public AjaxResult cleanInvalidImages()
    {
        System.out.println("=== 清理无效图片路径 ===");
        
        Advertisement query = new Advertisement();
        List<Advertisement> allList = advertisementService.selectAdvertisementList(query);
        
        int cleanedCount = 0;
        for (Advertisement ad : allList) {
            String afterImages = ad.getAfterImages();
            if (afterImages != null && !afterImages.isEmpty()) {
                String cleanedPath = afterImages.trim().replaceAll("^[\\s\"'`]+|[\\s\"'`]+$", "");
                
                boolean isTempPath = cleanedPath.startsWith("tmp/") || 
                    cleanedPath.startsWith("wxfile://") ||
                    cleanedPath.startsWith("http://tmp/") ||
                    cleanedPath.startsWith("https://tmp/") ||
                    cleanedPath.contains("/tmp/");
                
                if (isTempPath) {
                    ad.setAfterImages("");
                    advertisementService.updateAdvertisement(ad);
                    cleanedCount++;
                    System.out.println("清理广告ID " + ad.getId() + " 的无效图片路径: " + afterImages);
                }
            }
        }
        
        System.out.println("共清理 " + cleanedCount + " 条无效图片路径");
        return success("共清理 " + cleanedCount + " 条无效图片路径");
    }
    
    private List<String> parseJsonArray(String jsonStr) {
        List<String> result = new ArrayList<>();
        if (jsonStr == null || jsonStr.isEmpty()) {
            return result;
        }
        try {
            String content = jsonStr.trim();
            
            if (content.startsWith("\"") && content.endsWith("\"")) {
                content = content.substring(1, content.length() - 1);
            }
            
            if (!content.startsWith("[") || !content.endsWith("]")) {
                return result;
            }
            content = content.substring(1, content.length() - 1);
            String[] items = content.split(",");
            for (String item : items) {
                item = item.trim();
                if (item.startsWith("\"") && item.endsWith("\"")) {
                    item = item.substring(1, item.length() - 1);
                }
                if (!item.isEmpty()) {
                    result.add(item);
                }
            }
        } catch (Exception e) {
            System.out.println("解析JSON数组失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 规范化图片路径
     * 将JSON数组格式的URL（如 ["http://localhost:8080/profile/upload/xxx.jpg"]）
     * 转为逗号分隔的相对路径（如 /profile/upload/xxx.jpg）
     * 同时过滤掉微信小程序临时路径
     */
    private String normalizeImagePath(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return "";
        }
        
        String cleaned = imagePath.trim().replace("`", "");
        
        // 如果是JSON数组格式，解析并处理每个URL
        if (cleaned.startsWith("[")) {
            List<String> urls = parseJsonArray(cleaned);
            List<String> normalizedUrls = new ArrayList<>();
            for (String url : urls) {
                String normalized = extractRelativePath(url);
                if (!normalized.isEmpty()) {
                    normalizedUrls.add(normalized);
                }
            }
            return String.join(",", normalizedUrls);
        }
        
        // 单个URL或路径
        return extractRelativePath(cleaned);
    }
    
    /**
     * 从URL中提取相对路径
     * 如 http://localhost:8080/profile/upload/xxx.jpg -> /profile/upload/xxx.jpg
     * 过滤掉微信小程序临时路径（tmp/, wxfile://等）
     */
    private String extractRelativePath(String url) {
        if (url == null || url.isEmpty()) {
            return "";
        }
        
        String cleaned = url.trim().replace("\"", "").replace("'", "");
        
        // 过滤临时路径
        if (cleaned.startsWith("tmp/") || 
            cleaned.startsWith("wxfile://") ||
            cleaned.startsWith("http://tmp/") ||
            cleaned.startsWith("https://tmp/") ||
            cleaned.contains("/tmp/")) {
            return "";
        }
        
        // 如果包含 /profile/ ，提取相对路径部分
        if (cleaned.contains("/profile/")) {
            return cleaned.substring(cleaned.indexOf("/profile/"));
        }
        
        // 如果已经是相对路径（以/开头）
        if (cleaned.startsWith("/")) {
            return cleaned;
        }
        
        // 其他情况返回原路径
        return cleaned;
    }
    
    /**
     * 解析 Redis 中的字符串数组（可能带外层引号和转义）
     * 例如 "[\"医疗健康行业\",\"小说行业\"]" 或 "[医疗,小说]"
     */
    private List<String> parseRedisList(String raw)
    {
        List<String> list = new ArrayList<>();
        if (raw == null || raw.isEmpty()) return list;

        String value = raw.trim();
        // 去掉外层双引号（例如 Redis Desktop Manager 写入时的额外包裹）
        if (value.startsWith("\"") && value.endsWith("\"") && value.length() >= 2)
        {
            value = value.substring(1, value.length() - 1);
        }
        // 统一转义处理：把 \" 替换为 "
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
            System.out.println("解析Redis数组失败，尝试按逗号分割: " + e.getMessage());
            // 兜底：按逗号分割
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
     * 获取行业分类列表（从Redis）
     */
    @GetMapping("/getIndustryList")
    public AjaxResult getIndustryList()
    {
        System.out.println("=== 获取行业分类列表 ===");

        List<Map<String, String>> result = new ArrayList<>();

        try
        {
            if (redisTemplate == null) {
                System.out.println("redisTemplate 为空，使用默认数据");
            } else {
                String industryData = redisTemplate.opsForValue().get("industry:list");
                System.out.println("Redis原始数据: " + industryData);

                if (industryData != null && !industryData.isEmpty())
                {
                    List<String> items = parseRedisList(industryData);
                    System.out.println("解析后行业数量: " + items.size());
                    for (String item : items)
                    {
                        Map<String, String> map = new HashMap<>();
                        map.put("code", item);
                        map.put("name", item);
                        result.add(map);
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("获取行业分类失败: " + e.getMessage());
            e.printStackTrace();
        }

        if (result.isEmpty()) {
            System.out.println("Redis数据为空，使用默认行业分类");
            String[] defaultIndustries = {"医疗健康行业", "教育培训行业", "金融理财行业", "游戏娱乐行业", "电商购物行业", "餐饮美食行业", "旅游出行行业", "房地产行业", "汽车行业", "其他行业"};
            for (int i = 0; i < defaultIndustries.length; i++) {
                Map<String, String> map = new HashMap<>();
                map.put("code", String.valueOf(i));
                map.put("name", defaultIndustries[i]);
                result.add(map);
            }
        }

        System.out.println("返回行业分类数量: " + result.size());
        return success(result);
    }

    /**
     * 获取媒体类型列表（从Redis）
     */
    @GetMapping("/getMediumList")
    public AjaxResult getMediumList()
    {
        System.out.println("=== 获取媒体类型列表 ===");

        List<Map<String, String>> result = new ArrayList<>();

        try
        {
            if (redisTemplate == null) {
                System.out.println("redisTemplate 为空，使用默认数据");
            } else {
                String mediumData = redisTemplate.opsForValue().get("medium:list");
                System.out.println("Redis原始数据: " + mediumData);

                if (mediumData != null && !mediumData.isEmpty())
                {
                    List<String> items = parseRedisList(mediumData);
                    System.out.println("解析后媒体类型数量: " + items.size());
                    for (String item : items)
                    {
                        Map<String, String> map = new HashMap<>();
                        map.put("code", item);
                        map.put("name", item);
                        result.add(map);
                    }
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("获取媒体类型失败: " + e.getMessage());
            e.printStackTrace();
        }

        if (result.isEmpty()) {
            System.out.println("Redis数据为空，使用默认媒体类型");
            String[] defaultMediums = {"户外广告", "电视广告", "报纸广告", "网络广告", "社交媒体", "广播广告", "杂志广告", "其他媒体"};
            for (int i = 0; i < defaultMediums.length; i++) {
                Map<String, String> map = new HashMap<>();
                map.put("code", String.valueOf(i));
                map.put("name", defaultMediums[i]);
                result.add(map);
            }
        }

        System.out.println("返回媒体类型数量: " + result.size());
        return success(result);
    }
}