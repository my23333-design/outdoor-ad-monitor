package com.ruoyi.advertisement.controller;

import java.util.List;
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
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.advertisement.domain.AuditRecords;
import com.ruoyi.advertisement.service.IAuditRecordsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 审核记录Controller
 * 
 * @author ruoyi
 * @date 2026-03-18
 */
@RestController
@RequestMapping("/advertisement/records")
public class AuditRecordsController extends BaseController
{
    @Autowired
    private IAuditRecordsService auditRecordsService;

    /**
     * 查询审核记录列表
     */
    @PreAuthorize("@ss.hasPermi('advertisement:records:list')")
    @GetMapping("/list")
    public TableDataInfo list(AuditRecords auditRecords)
    {
        startPage();
        List<AuditRecords> list = auditRecordsService.selectAuditRecordsList(auditRecords);
        return getDataTable(list);
    }

    /**
     * 导出审核记录列表
     */
    @PreAuthorize("@ss.hasPermi('advertisement:records:export')")
    @Log(title = "审核记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AuditRecords auditRecords)
    {
        List<AuditRecords> list = auditRecordsService.selectAuditRecordsList(auditRecords);
        ExcelUtil<AuditRecords> util = new ExcelUtil<AuditRecords>(AuditRecords.class);
        util.exportExcel(response, list, "审核记录数据");
    }

    /**
     * 获取审核记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('advertisement:records:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(auditRecordsService.selectAuditRecordsById(id));
    }

    /**
     * 新增审核记录
     */
    @PreAuthorize("@ss.hasPermi('advertisement:records:add')")
    @Log(title = "审核记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AuditRecords auditRecords)
    {
        return toAjax(auditRecordsService.insertAuditRecords(auditRecords));
    }

    /**
     * 修改审核记录
     */
    @PreAuthorize("@ss.hasPermi('advertisement:records:edit')")
    @Log(title = "审核记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AuditRecords auditRecords)
    {
        return toAjax(auditRecordsService.updateAuditRecords(auditRecords));
    }

    /**
     * 删除审核记录
     */
    @PreAuthorize("@ss.hasPermi('advertisement:records:remove')")
    @Log(title = "审核记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(auditRecordsService.deleteAuditRecordsByIds(ids));
    }
}
