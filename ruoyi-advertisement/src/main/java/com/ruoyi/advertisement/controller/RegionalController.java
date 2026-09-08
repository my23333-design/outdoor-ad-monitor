package com.ruoyi.advertisement.controller;

import java.util.List;
import java.util.Map;
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
import com.ruoyi.advertisement.domain.Regional;
import com.ruoyi.advertisement.service.IRegionalService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 行政区域管理Controller
 * 
 * @author wanghao
 * @date 2025-12-04
 */
@RestController
@RequestMapping("/advertisement/regional")
public class RegionalController extends BaseController
{
    @Autowired
    private IRegionalService regionalService;

    /**
     * 查询行政区域管理列表
     */
    @PreAuthorize("@ss.hasPermi('advertisement:regional:list')")
    @GetMapping("/list")
    public TableDataInfo list(Regional regional)
    {
        System.out.println("查询参数:");
        System.out.println("name: " + regional.getName());
        System.out.println("parent: " + regional.getParent());
        System.out.println("sepll: " + regional.getSepll());
        System.out.println("type: " + regional.getType());
        System.out.println("level: " + regional.getLevel());
        startPage();
        List<Regional> list = regionalService.selectRegionalList(regional);
        System.out.println("查询结果数量: " + list.size());
        return getDataTable(list);
    }

    /**
     * 导出行政区域管理列表
     */
    @PreAuthorize("@ss.hasPermi('advertisement:regional:export')")
    @Log(title = "行政区域管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Regional regional)
    {
        List<Regional> list = regionalService.selectRegionalList(regional);
        ExcelUtil<Regional> util = new ExcelUtil<Regional>(Regional.class);
        util.exportExcel(response, list, "行政区域管理数据");
    }

    /**
     * 获取行政区域管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('advertisement:regional:query')")
    @GetMapping(value = "/{code}")
    public AjaxResult getInfo(@PathVariable("code") String code)
    {
        return success(regionalService.selectRegionalByCode(code));
    }

    /**
     * 新增行政区域管理
     */
    @PreAuthorize("@ss.hasPermi('advertisement:regional:add')")
    @Log(title = "行政区域管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Regional regional)
    {
        return toAjax(regionalService.insertRegional(regional));
    }

    /**
     * 修改行政区域管理
     */
    @PreAuthorize("@ss.hasPermi('advertisement:regional:edit')")
    @Log(title = "行政区域管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Regional regional)
    {
        return toAjax(regionalService.updateRegional(regional));
    }

    /**
     * 删除行政区域管理
     */
    @PreAuthorize("@ss.hasPermi('advertisement:regional:remove')")
    @Log(title = "行政区域管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{codes}")
    public AjaxResult remove(@PathVariable String[] codes)
    {
        return toAjax(regionalService.deleteRegionalByCodes(codes));
    }

    /**
     * 获取行政区域树形结构
     */
    @GetMapping("/tree")
    public AjaxResult getTree()
    {
        List<Map<String, Object>> tree = regionalService.getRegionalTree();
        return success(tree);
    }
}