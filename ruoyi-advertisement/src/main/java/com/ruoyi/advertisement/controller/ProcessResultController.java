package com.ruoyi.advertisement.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.advertisement.domain.Enforcement;
import com.ruoyi.advertisement.domain.Advertisement;
import com.ruoyi.advertisement.service.IEnforcementService;
import com.ruoyi.advertisement.service.IAdvertisementService;
import com.ruoyi.common.utils.pdf.PdfReportUtil;
import com.ruoyi.common.annotation.Anonymous;

@RestController
@RequestMapping("/process/result")
public class ProcessResultController extends BaseController {

    @Autowired
    private IEnforcementService enforcementService;
    
    @Autowired
    private IAdvertisementService advertisementService;

    @GetMapping("/list")
    public TableDataInfo list(Enforcement enforcement) {
        startPage();
        return getDataTable(enforcementService.selectEnforcementList(enforcement));
    }

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(enforcementService.selectEnforcementById(id));
    }

    @Anonymous
    @GetMapping("/report/{id}")
    public void generateReport(@PathVariable("id") Long id, HttpServletResponse response) {
        try {
            Enforcement enforcement = enforcementService.selectEnforcementById(id);
            if (enforcement == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setContentType("text/plain");
                response.getWriter().write("执行结果记录不存在");
                return;
            }

            Advertisement ad = null;
            if (enforcement.getAdId() != null) {
                ad = advertisementService.selectAdvertisementById(enforcement.getAdId());
            }

            Map<String, Object> data = new HashMap<>();
            
            String profitabilityType = "-";
            String industryType = "-";
            String mediumType = "-";
            String address = "-";
            String preHandleImage = null;
            
            if (ad != null) {
                profitabilityType = ad.getAdProfitabilityType() != null && "1".equals(ad.getAdProfitabilityType()) ? "公益广告" : "商业广告";
                industryType = ad.getAdIndustryType() != null ? ad.getAdIndustryType() : "-";
                mediumType = ad.getAdMediumType() != null ? ad.getAdMediumType() : "-";
                
                StringBuilder addr = new StringBuilder();
                if (ad.getProvince() != null && !ad.getProvince().isEmpty()) addr.append(ad.getProvince());
                if (ad.getCity() != null && !ad.getCity().isEmpty()) addr.append(ad.getCity());
                if (ad.getDistrict() != null && !ad.getDistrict().isEmpty()) addr.append(ad.getDistrict());
                if (ad.getStreet() != null && !ad.getStreet().isEmpty()) addr.append(ad.getStreet());
                address = addr.length() > 0 ? addr.toString() : "-";
                
                preHandleImage = ad.getAdImages();
                if (preHandleImage != null && !preHandleImage.isEmpty() && !preHandleImage.equals("null")) {
                    if (preHandleImage.startsWith("[")) {
                        try {
                            int startIdx = preHandleImage.indexOf("\"") + 1;
                            int endIdx = preHandleImage.indexOf("\"", startIdx);
                            if (startIdx > 0 && endIdx > startIdx) {
                                preHandleImage = preHandleImage.substring(startIdx, endIdx);
                            }
                        } catch (Exception e) {
                        }
                    }
                    if (!preHandleImage.startsWith("http://") && !preHandleImage.startsWith("https://")) {
                        preHandleImage = "http://localhost:8080" + preHandleImage;
                    }
                }
            }
            
            data.put("profitabilityType", profitabilityType);
            data.put("industryType", industryType);
            data.put("mediumType", mediumType);
            data.put("handleResult", enforcement.getHandleResult() != null ? enforcement.getHandleResult() : "-");
            data.put("handler", enforcement.getHandler() != null ? enforcement.getHandler() : "-");
            
            String handleTimeStr = "-";
            if (enforcement.getHandleTime() != null) {
                handleTimeStr = enforcement.getHandleTime().toString();
            }
            data.put("handleTime", handleTimeStr);
            
            data.put("address", address);
            data.put("preHandleImage", preHandleImage);
            
            String postHandleImage = enforcement.getPostHandleImage();
            if (postHandleImage != null && !postHandleImage.isEmpty() && !postHandleImage.equals("null")) {
                if (postHandleImage.startsWith("[")) {
                    try {
                        int startIdx = postHandleImage.indexOf("\"") + 1;
                        int endIdx = postHandleImage.indexOf("\"", startIdx);
                        if (startIdx > 0 && endIdx > startIdx) {
                            postHandleImage = postHandleImage.substring(startIdx, endIdx);
                        }
                    } catch (Exception e) {
                    }
                }
                if (!postHandleImage.startsWith("http://") && !postHandleImage.startsWith("https://")) {
                    postHandleImage = "http://localhost:8080" + postHandleImage;
                }
            }
            data.put("postHandleImage", postHandleImage);

            byte[] pdfBytes = PdfReportUtil.generateAdProcessReport(data);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=ad_process_report_" + id + ".pdf");
            response.setContentLength(pdfBytes.length);
            response.getOutputStream().write(pdfBytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.setContentType("text/plain");
                response.getWriter().write("生成PDF报告失败: " + e.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}