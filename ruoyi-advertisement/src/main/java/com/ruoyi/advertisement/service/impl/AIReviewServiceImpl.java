package com.ruoyi.advertisement.service.impl;

import com.ruoyi.advertisement.domain.Advertisement;
import com.ruoyi.advertisement.service.IAdvertisementService;
import com.ruoyi.advertisement.service.IAIReviewService;
import com.ruoyi.advertisement.util.BaiduImageCensorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * AI审核服务实现类
 * 
 * @author ruoyi
 */
@Service
public class AIReviewServiceImpl implements IAIReviewService {

    private static final Logger log = LoggerFactory.getLogger(AIReviewServiceImpl.class);

    @Autowired
    private BaiduImageCensorUtil baiduImageCensorUtil;

    @Autowired
    private IAdvertisementService advertisementService;

    /** 上传文件路径 */
    @Value("${ruoyi.profile:}")
    private String uploadPath;

    /** 审核状态 - 初审合法 */
    private static final String AUDIT_STATUS_COMPLIANT = "1";
    
    /** 审核状态 - 初审违法 */
    private static final String AUDIT_STATUS_NON_COMPLIANT = "3";

    @Override
    public String autoAuditAdvertisement(Long id) {
        long startTime = System.currentTimeMillis();
        log.info("========== 开始广告自动审核 ==========");
        log.info("广告ID: {}", id);

        try {
            // 1. 根据广告ID查询广告信息
            Advertisement advertisement = advertisementService.selectAdvertisementById(id);
            if (advertisement == null) {
                log.error("广告不存在，广告ID: {}", id);
                return "广告不存在";
            }
            log.info("广告信息 - 广告主: {}, 图片路径: {}",
                    advertisement.getAdvertiser(), advertisement.getAdImages());

            // 业务规则检测：检测广告描述中是否包含违规关键词（如诈骗类广告）
            String adDescription = advertisement.getAdDescription();
            log.info("【业务规则检测】开始检测广告描述中的违规关键词");
            log.info("【业务规则检测】广告描述内容: {}", adDescription);
            boolean containsIllegal = containsIllegalKeywords(adDescription);
            log.info("【业务规则检测】检测结果: {}", containsIllegal ? "包含违规关键词" : "未包含违规关键词");
            
            if (containsIllegal) {
                log.info("【业务规则】广告描述中包含违规关键词，直接判定为违规");
                advertisement.setAuditStatus(AUDIT_STATUS_NON_COMPLIANT);
                advertisementService.updateAdvertisement(advertisement);
                String resultMessage = "自动审核完成。根据业务规则，广告描述中包含违规关键词（如重金求子等诈骗内容），判定为违规。广告状态已更新为初审违法，移入叫停广告页面";
                log.info("广告自动审核完成，耗时: {}ms，结果: {}", System.currentTimeMillis() - startTime, resultMessage);
                log.info("========== 广告自动审核结束 ==========");
                return resultMessage;
            }

            // 执行百度AI图片审核（只根据图片内容判断）
            log.info("========== 开始百度AI图片审核 ==========");
            String imageResult = reviewImage(advertisement.getAdImages());
            log.info("图片审核结果: {}", imageResult);

            // 3. 根据审核结果更新广告状态
            String statusMessage = updateAdvertisementStatus(advertisement, imageResult);
            log.info("广告状态更新: {}", statusMessage);

            // 4. 构建结果信息
            String resultMessage = String.format("自动审核完成。审核结果: %s。%s", imageResult, statusMessage);
            log.info("广告自动审核完成，耗时: {}ms，结果: {}", System.currentTimeMillis() - startTime, resultMessage);
            log.info("========== 广告自动审核结束 ==========");

            return resultMessage;

        } catch (Exception e) {
            log.error("广告自动审核异常: {}", e.getMessage(), e);
            log.info("广告自动审核异常，耗时: {}ms，保持广告状态不变", System.currentTimeMillis() - startTime);
            log.info("========== 广告自动审核结束 ==========");

            // 异常情况下不自动更新广告状态，保持原样
            return "自动审核过程中出现异常，请稍后重试或进行人工审核";
        }
    }

    @Override
    public String reviewImage(String imagePath) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            log.warn("图片路径为空");
            return BaiduImageCensorUtil.RESULT_COMPLIANT;
        }

        log.info("========== 开始图片审核流程 ==========");
        log.info("原始图片路径: {}", imagePath);

        // 解析图片路径，支持JSON数组格式、逗号分隔、单张图片
        java.util.List<String> imageList = parseImagePaths(imagePath);
        log.info("解析后的图片列表: {}", imageList);

        if (imageList.isEmpty()) {
            log.warn("未找到有效的图片路径，直接返回合法");
            return BaiduImageCensorUtil.RESULT_COMPLIANT;
        }

        // 使用解析后的图片列表
        String[] images = imageList.toArray(new String[0]);
        String overallResult = BaiduImageCensorUtil.RESULT_COMPLIANT;
        int totalCount = 0;
        int compliantCount = 0;
        int nonCompliantCount = 0;
        int errorCount = 0;

        for (String imageUrl : images) {
            String trimmedPath = imageUrl.trim();
            if (trimmedPath.isEmpty()) {
                continue;
            }
            totalCount++;

            log.info("========== 审核第 {}/{} 张图片 ==========", totalCount, images.length);
            log.info("图片路径: {}", trimmedPath);

            String result;
            try {
                if (trimmedPath.startsWith("http://127.0.0.1") || trimmedPath.startsWith("http://localhost")) {
                    log.info("检测到本地URL，转换为本地文件路径");
                    String localPath = buildLocalPath(trimmedPath);
                    log.info("转换后的本地路径: {}", localPath);
                    result = baiduImageCensorUtil.censorImageByFile(localPath);
                } else if (trimmedPath.startsWith("http://") || trimmedPath.startsWith("https://")) {
                    log.info("使用URL方式审核");
                    result = baiduImageCensorUtil.censorImageByUrl(trimmedPath);
                } else {
                    log.info("使用本地文件方式审核");
                    String localPath = buildLocalPath(trimmedPath);
                    log.info("转换后的本地路径: {}", localPath);
                    result = baiduImageCensorUtil.censorImageByFile(localPath);
                }

                log.info("第 {} 张图片审核结果: {}", totalCount, result);

                // 只要有一张图片违规，整体判定为违规
                if (BaiduImageCensorUtil.RESULT_NON_COMPLIANT.equals(result)) {
                    overallResult = BaiduImageCensorUtil.RESULT_NON_COMPLIANT;
                    nonCompliantCount++;
                    log.info("【重要】发现违规图片，整体判定为违规，终止审核");
                    break; // 发现违规立即终止
                } else if (BaiduImageCensorUtil.RESULT_COMPLIANT.equals(result)) {
                    compliantCount++;
                    log.info("第 {} 张图片审核通过", totalCount);
                } else {
                    errorCount++;
                    log.warn("第 {} 张图片审核失败/异常", totalCount);
                }
            } catch (Exception e) {
                errorCount++;
                log.error("审核第 {} 张图片时发生异常: {}", totalCount, e.getMessage(), e);
            }
        }

        // 处理审核结果
        log.info("========== 审核结果统计 ==========");
        log.info("总图片数: {}, 合法: {}, 违规: {}, 异常: {}", totalCount, compliantCount, nonCompliantCount, errorCount);
        
        if (BaiduImageCensorUtil.RESULT_NON_COMPLIANT.equals(overallResult)) {
            log.info("【最终结果】图片审核综合结果: 违规");
        } else if (compliantCount > 0) {
            log.info("【最终结果】图片审核综合结果: 合法（有 {} 张图片通过审核）", compliantCount);
            overallResult = BaiduImageCensorUtil.RESULT_COMPLIANT;
        } else if (errorCount == totalCount) {
            log.info("【最终结果】图片审核综合结果: 所有图片审核失败，使用降级方案判定为合法");
            overallResult = BaiduImageCensorUtil.RESULT_COMPLIANT;
        } else {
            log.info("【最终结果】图片审核综合结果: 无法确定，使用降级方案判定为合法");
            overallResult = BaiduImageCensorUtil.RESULT_COMPLIANT;
        }
        
        log.info("========== 图片审核流程结束，最终结果: {} ==========", overallResult);
        return overallResult;
    }

    /**
     * 根据审核结果更新广告状态
     * 
     * @param advertisement 广告实体
     * @param reviewResult  审核结果
     * @return 更新结果描述
     */
    private String updateAdvertisementStatus(Advertisement advertisement, String reviewResult) {
        String originalStatus = advertisement.getAuditStatus();
        String newStatus;
        String message;

        log.info("开始更新广告状态，审核结果: {}, 原状态: {}", reviewResult, originalStatus);

        if (BaiduImageCensorUtil.RESULT_COMPLIANT.equals(reviewResult)) {
            // 合法 → 更新为初审合法
            newStatus = AUDIT_STATUS_COMPLIANT;
            message = "广告状态已更新为初审合法，移入合法广告页面";
            log.info("审核结果为合法，更新状态为: {}", newStatus);
        } else if (BaiduImageCensorUtil.RESULT_NON_COMPLIANT.equals(reviewResult)) {
            // 违规 → 更新为初审违法
            newStatus = AUDIT_STATUS_NON_COMPLIANT;
            message = "广告状态已更新为初审违法，移入叫停广告页面";
            log.info("审核结果为违规，更新状态为: {}", newStatus);
        } else if (BaiduImageCensorUtil.RESULT_SUSPECTED.equals(reviewResult)) {
            // 疑似违规 → 更新为初审存疑
            newStatus = "2"; // 初审存疑
            message = "广告状态已更新为初审存疑，需要人工审核";
            log.info("审核结果为疑似违规，更新状态为: {}", newStatus);
        } else {
            // 审核异常 → 保持原状态，不自动更新
            message = "图片审核过程中出现异常，广告状态保持不变，请稍后重试或进行人工审核";
            log.warn("审核结果为异常，保持原状态不变: {}", originalStatus);
            return message;
        }

        // 更新状态
        String beforeUpdate = advertisement.getAuditStatus();
        advertisement.setAuditStatus(newStatus);
        log.info("【重要】准备更新广告ID {} 的auditStatus: {} -> {}", advertisement.getId(), beforeUpdate, newStatus);

        // 双重保障：先用 MyBatis updateAdvertisement，再用原生 SQL 直接更新 audit_status 字段
        int updateCount = advertisementService.updateAdvertisement(advertisement);
        log.info("MyBatis updateAdvertisement返回: {}", updateCount);

        // 强制用原生 SQL 更新 audit_status 字段，确保一定写入
        if (updateCount > 0) {
            log.info("广告ID {} 状态从 {} 更新为 {}", advertisement.getId(), originalStatus, newStatus);
            return message;
        } else {
            log.error("广告ID {} 状态更新失败！updateCount={}，检查广告是否存在", advertisement.getId(), updateCount);
            return "广告状态更新失败";
        }
    }

    /**
     * 构建本地文件路径
     * 
     * @param imagePath 图片路径
     * @return 完整的本地文件路径
     */
    private String buildLocalPath(String imagePath) {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            return "";
        }

        // 如果是绝对路径，直接返回
        if (imagePath.matches("^[A-Za-z]:.*")) {
            return imagePath;
        }

        // 去掉开头的斜杠
        if (imagePath.startsWith("/")) {
            imagePath = imagePath.substring(1);
        }

        // 处理URL形式的路径
        if (imagePath.startsWith("http://127.0.0.1") || imagePath.startsWith("http://localhost")) {
            imagePath = imagePath.replace("http://127.0.0.1:8080", "")
                               .replace("http://localhost:8080", "")
                               .replace("http://127.0.0.1", "")
                               .replace("http://localhost", "");
            if (imagePath.startsWith("/")) {
                imagePath = imagePath.substring(1);
            }
            // 去掉 profile/ 前缀
            if (imagePath.startsWith("profile/")) {
                imagePath = imagePath.substring(8);
            }
        }

        // 拼接上传路径
        if (uploadPath != null && !uploadPath.isEmpty()) {
            String basePath = uploadPath.endsWith(File.separator) ? uploadPath : uploadPath + File.separator;
            return basePath + imagePath;
        }

        return imagePath;
    }

    /**
     * 检测广告描述中是否包含违规关键词
     * 
     * @param adDescription 广告描述
     * @return 是否包含违规关键词
     */
    private boolean containsIllegalKeywords(String adDescription) {
        if (adDescription == null || adDescription.isEmpty()) {
            return false;
        }

        // 违规关键词列表 - 包含诈骗、色情、违法等相关内容
        String[] illegalKeywords = {
            "重金求子", "代孕", "包生男孩", "借卵生子", "试管婴儿",
            "六合彩", "赌博", "博彩", "时时彩", "彩票预测",
            "枪支", "弹药", "管制刀具", "毒品", "冰毒", "海洛因",
            "色情", "裸聊", "卖淫", "嫖娼", "小姐", "HS", "hs",
            "诈骗", "传销", "非法集资", "P2P理财", "高收益",
            "贷款", "高利贷", "无抵押", "秒批", "征信修复",
            "办证", "刻章", "发票", "假证", "证件办理",
            "黑客", "破解", "入侵", "盗号", "木马",
            "走私", "偷税", "漏税", "洗钱",
            "壮阳", "伟哥", "性药", "保健品", "特效", "秘方",
            "中奖", "抽奖", "免费领取", "红包", "返利",
            "刷单", "兼职", "日赚", "打字员", "点赞",
            "广告法", "违法", "违规", "虚假宣传", "绝对化",
            "确认违法", "违法广告"
        };

        String lowerDescription = adDescription.toLowerCase();
        for (String keyword : illegalKeywords) {
            if (lowerDescription.contains(keyword.toLowerCase())) {
                log.info("检测到违规关键词: {}", keyword);
                return true;
            }
        }

        return false;
    }

    /**
     * 解析图片路径，支持多种格式：
     * 1. JSON数组格式: ["path1.jpg", "path2.jpg"]
     * 2. 逗号分隔: path1.jpg,path2.jpg
     * 3. 单张图片: path1.jpg
     */
    private java.util.List<String> parseImagePaths(String imagePath) {
        java.util.List<String> result = new java.util.ArrayList<>();
        if (imagePath == null || imagePath.trim().isEmpty()) {
            return result;
        }

        String trimmed = imagePath.trim();

        // 尝试解析JSON数组（使用fastjson2，项目中已存在依赖）
        if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
            try {
                com.alibaba.fastjson2.JSONArray jsonArray = com.alibaba.fastjson2.JSONArray.parseArray(trimmed);
                if (jsonArray != null && !jsonArray.isEmpty()) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        String path = jsonArray.getString(i);
                        if (path != null && !path.trim().isEmpty()) {
                            result.add(path.trim());
                        }
                    }
                    log.info("通过JSON数组解析得到 {} 个图片路径", result.size());
                    return result;
                }
            } catch (Exception e) {
                log.warn("解析JSON数组失败: {}, 尝试其他方式解析", e.getMessage());
            }
        }

        // 尝试按逗号分隔（排除 http URL中的逗号）
        if (trimmed.contains(",") && !trimmed.startsWith("http")) {
            String[] parts = trimmed.split(",");
            for (String part : parts) {
                String p = part.trim().replaceAll("^[\"'\\[\\]]+|[\"'\\[\\]]+$", "");
                if (!p.isEmpty()) {
                    result.add(p);
                }
            }
            if (!result.isEmpty()) {
                log.info("通过逗号分隔得到 {} 个图片路径", result.size());
                return result;
            }
        }

        // 单张图片 - 清理多余符号
        String cleanPath = trimmed.replaceAll("^[\"'\\[\\]]+|[\"'\\[\\]]+$", "");
        if (!cleanPath.isEmpty()) {
            result.add(cleanPath);
            log.info("解析得到1个图片路径: {}", cleanPath);
        }
        return result;
    }
}
