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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.advertisement.domain.LawClauses;
import com.ruoyi.advertisement.service.ILawClausesService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 广告法律条款管理Controller
 * 
 * @author wanghao
 * @date 2025-12-08
 */
@RestController
    @RequestMapping("/advertisement/clauses")
public class LawClausesController extends BaseController
{
    @Autowired
    private ILawClausesService lawClausesService;

    /**
     * 查询广告法律条款管理列表
     */
    @PreAuthorize("@ss.hasPermi('advertisement:clauses:list')")
    @GetMapping("/list")
    public TableDataInfo list(LawClauses lawClauses)
    {
        startPage();
        //只查询未删除的数据
        lawClauses.setIsDeleted(0);
        List<LawClauses> list = lawClausesService.selectLawClausesList(lawClauses);
        return getDataTable(list);
    }
    /**
     * 不带分页的所有数据，查询广告法律条款管理列表
     */
    @PreAuthorize("@ss.hasPermi('advertisement:clauses:list')")
    @GetMapping("/listAll")
    public AjaxResult listAll(LawClauses lawClauses)
    {

        List<LawClauses> list = lawClausesService.selectLawClausesList(lawClauses);
        return success(list);
    }

    /**
     * 导出广告法律条款管理列表
     */
    @PreAuthorize("@ss.hasPermi('advertisement:clauses:export')")
    @Log(title = "广告法律条款管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LawClauses lawClauses)
    {
        List<LawClauses> list = lawClausesService.selectLawClausesList(lawClauses);
        ExcelUtil<LawClauses> util = new ExcelUtil<LawClauses>(LawClauses.class);
        util.exportExcel(response, list, "广告法律条款管理数据");
    }

    /**
     * 检查法律编号是否已存在
     */
    @GetMapping("/checkCode")
    public AjaxResult checkCode(@RequestParam("clauseCode") String clauseCode, 
                                @RequestParam(value = "id", required = false) Long id)
    {
        LawClauses lawClauses = new LawClauses();
        lawClauses.setClauseCode(clauseCode);
        lawClauses.setIsDeleted(0);
        List<LawClauses> list = lawClausesService.selectLawClausesList(lawClauses);
        
        boolean exists = false;
        if (list != null && !list.isEmpty()) {
            // 如果是修改操作，需要排除自身
            if (id != null) {
                for (LawClauses item : list) {
                    if (!item.getId().equals(id)) {
                        exists = true;
                        break;
                    }
                }
            } else {
                exists = true;
            }
        }
        return success(exists);
    }

    /**
     * 检查法律名称和条款编号组合是否已存在
     */
    @PostMapping("/checkUnique")
    public AjaxResult checkUnique(@RequestBody java.util.Map<String, Object> params)
    {
        String lawName = (String) params.get("lawName");
        String clauseNumber = (String) params.get("clauseNumber");
        Long id = params.get("id") != null ? Long.valueOf(params.get("id").toString()) : null;
        
        LawClauses lawClauses = new LawClauses();
        lawClauses.setLawName(lawName);
        lawClauses.setClauseNumber(clauseNumber);
        lawClauses.setIsDeleted(0);
        List<LawClauses> list = lawClausesService.selectLawClausesList(lawClauses);
        
        boolean exists = false;
        if (list != null && !list.isEmpty()) {
            // 如果是修改操作，需要排除自身
            if (id != null) {
                for (LawClauses item : list) {
                    if (!item.getId().equals(id)) {
                        exists = true;
                        break;
                    }
                }
            } else {
                exists = true;
            }
        }
        return success(exists);
    }

    /**
     * 获取广告法律条款管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('advertisement:clauses:query')")
    @GetMapping(value = "/detail/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(lawClausesService.selectLawClausesById(id));
    }

    /**
     * 新增广告法律条款管理
     */
    @PreAuthorize("@ss.hasPermi('advertisement:clauses:add')")
    @Log(title = "广告法律条款管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LawClauses lawClauses)
    {
        int row = lawClausesService.insertLawClauses(lawClauses);
        if(row == -1){
            return error("该法律条款编码已存在");
        }
        return toAjax(row);
    }

    /**
     * 修改广告法律条款管理
     */
    @PreAuthorize("@ss.hasPermi('advertisement:clauses:edit')")
    @Log(title = "广告法律条款管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LawClauses lawClauses)
    {
        int row = lawClausesService.updateLawClauses(lawClauses);
        if(row == -1){
            return error("该法律条款编码已存在");
        }
        return toAjax(row);
    }

    /**
     * 删除广告法律条款管理
     */
    @PreAuthorize("@ss.hasPermi('advertisement:clauses:remove')")
    @Log(title = "广告法律条款管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(lawClausesService.deleteLawClausesByIdsAndIsDelete(ids));
    }

}
