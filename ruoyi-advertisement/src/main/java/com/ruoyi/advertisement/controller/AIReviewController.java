package com.ruoyi.advertisement.controller;

import com.ruoyi.advertisement.service.IAIReviewService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * AI审核控制器
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/advertisement/aiReview")
public class AIReviewController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(AIReviewController.class);

    @Autowired
    private IAIReviewService aiReviewService;

    /**
     * 自动审核广告
     * 根据广告ID查询数据，执行百度AI图片审核，更新广告状态
     * 
     * @param id 广告ID
     * @return 审核结果
     */
    @PostMapping("/autoAudit")
    public AjaxResult autoAudit(@RequestParam Long id) {
        try {
            log.info("收到自动审核请求，广告ID: {}", id);
            String result = aiReviewService.autoAuditAdvertisement(id);
            return AjaxResult.success(result);
        } catch (Exception e) {
            log.error("自动审核异常: {}", e.getMessage(), e);
            return AjaxResult.error("自动审核失败: " + e.getMessage());
        }
    }

    /**
     * 审核图片（GET方法，兼容前端）
     * 
     * @param adId 广告ID
     * @param imagePath 图片路径（URL或本地路径）
     * @return 审核结果
     */
    @GetMapping("/auditImage")
    public AjaxResult auditImage(@RequestParam Long adId, @RequestParam String imagePath) {
        try {
            log.info("收到图片审核请求(GET)，广告ID: {}, 图片路径: {}", adId, imagePath);
            String result = aiReviewService.autoAuditAdvertisement(adId);
            return AjaxResult.success("AI审核完成，结果已保存作为人工审核参考: " + result);
        } catch (Exception e) {
            log.error("AI审核异常: {}", e.getMessage(), e);
            return AjaxResult.error("AI审核失败: " + e.getMessage());
        }
    }

    /**
     * 审核图片（POST方法，单独调用）
     * 
     * @param imagePath 图片路径（URL或本地路径）
     * @return 审核结果
     */
    @PostMapping("/reviewImage")
    public AjaxResult reviewImage(@RequestParam String imagePath) {
        try {
            log.info("收到图片审核请求，图片路径: {}", imagePath);
            String result = aiReviewService.reviewImage(imagePath);
            return AjaxResult.success("图片审核完成: " + result, result);
        } catch (Exception e) {
            log.error("图片审核异常: {}", e.getMessage(), e);
            return AjaxResult.error("图片审核失败: " + e.getMessage());
        }
    }

    /**
     * 审核广告并更新状态（兼容前端旧接口）
     * 
     * @param adId 广告ID
     * @param imagePath 图片路径
     * @return 审核结果
     */
    @PostMapping("/reviewAndUpdate")
    public AjaxResult reviewAndUpdate(@RequestParam Long adId, @RequestParam String imagePath) {
        try {
            log.info("收到AI审核请求，广告ID: {}, 图片路径: {}", adId, imagePath);
            String result = aiReviewService.autoAuditAdvertisement(adId);
            return AjaxResult.success("AI审核完成，结果已保存作为人工审核参考: " + result);
        } catch (Exception e) {
            log.error("AI审核异常: {}", e.getMessage(), e);
            return AjaxResult.error("AI审核失败: " + e.getMessage());
        }
    }

    /**
     * 批量审核广告（支持多个广告ID）
     * 
     * @param ids 广告ID列表（逗号分隔）
     * @return 审核结果
     */
    @PostMapping("/batchAudit")
    public AjaxResult batchAudit(@RequestParam String ids) {
        try {
            log.info("收到批量审核请求，广告ID: {}", ids);
            String[] idArray = ids.split(",");
            StringBuilder resultBuilder = new StringBuilder();
            int successCount = 0;
            int failCount = 0;

            for (String idStr : idArray) {
                try {
                    Long id = Long.parseLong(idStr.trim());
                    String result = aiReviewService.autoAuditAdvertisement(id);
                    resultBuilder.append("广告ID ").append(id).append(": ").append(result).append("; ");
                    successCount++;
                } catch (NumberFormatException e) {
                    resultBuilder.append("无效的广告ID: ").append(idStr).append("; ");
                    failCount++;
                } catch (Exception e) {
                    resultBuilder.append("广告ID ").append(idStr).append("审核失败: ").append(e.getMessage()).append("; ");
                    failCount++;
                }
            }

            String summary = String.format("批量审核完成，成功: %d，失败: %d。详情: %s", 
                    successCount, failCount, resultBuilder.toString());
            log.info(summary);
            return AjaxResult.success(summary);
        } catch (Exception e) {
            log.error("批量审核异常: {}", e.getMessage(), e);
            return AjaxResult.error("批量审核失败: " + e.getMessage());
        }
    }
}
