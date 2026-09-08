package com.ruoyi.advertisement.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.advertisement.mapper.RegionalMapper;
import com.ruoyi.advertisement.domain.Regional;
import com.ruoyi.advertisement.service.IRegionalService;

/**
 * 行政区域管理Service业务层处理
 * 
 * @author wanghao
 * @date 2025-12-04
 */
@Service
public class RegionalServiceImpl implements IRegionalService 
{
    @Autowired
    private RegionalMapper regionalMapper;

    /**
     * 查询行政区域管理
     * 
     * @param code 行政区域管理主键
     * @return 行政区域管理
     */
    @Override
    public Regional selectRegionalByCode(String code)
    {
        return regionalMapper.selectRegionalByCode(code);
    }

    /**
     * 查询行政区域管理列表
     * 
     * @param regional 行政区域管理
     * @return 行政区域管理
     */
    @Override
    public List<Regional> selectRegionalList(Regional regional)
    {
        return regionalMapper.selectRegionalList(regional);
    }

    /**
     * 新增行政区域管理
     * 
     * @param regional 行政区域管理
     * @return 结果
     */
    @Override
    public int insertRegional(Regional regional)
    {
        return regionalMapper.insertRegional(regional);
    }

    /**
     * 修改行政区域管理
     * 
     * @param regional 行政区域管理
     * @return 结果
     */
    @Override
    public int updateRegional(Regional regional)
    {
        return regionalMapper.updateRegional(regional);
    }

    /**
     * 批量删除行政区域管理
     * 
     * @param codes 需要删除的行政区域管理主键
     * @return 结果
     */
    @Override
    public int deleteRegionalByCodes(String[] codes)
    {
        return regionalMapper.deleteRegionalByCodes(codes);
    }

    /**
     * 删除行政区域管理信息
     * 
     * @param code 行政区域管理主键
     * @return 结果
     */
    @Override
    public int deleteRegionalByCode(String code)
    {
        return regionalMapper.deleteRegionalByCode(code);
    }

    /**
     * 获取行政区域树形结构
     * 
     * @return 行政区域树形结构
     */
    @Override
    public List<Map<String, Object>> getRegionalTree()
    {
        System.out.println("开始构建行政区域树形结构");
        long startTime = System.currentTimeMillis();
        
        // 获取所有行政区域数据
        List<Regional> regionalList = regionalMapper.selectRegionalList(new Regional());
        System.out.println("获取到行政区域数据：" + regionalList.size() + " 条，耗时：" + (System.currentTimeMillis() - startTime) + "ms");
        
        // 构建树形结构
        List<Map<String, Object>> tree = new ArrayList<>();
        Map<String, Map<String, Object>> nodeMap = new HashMap<>(regionalList.size());
        
        // 先创建所有节点
        for (Regional regional : regionalList) {
            Map<String, Object> node = new HashMap<>(4);
            node.put("id", regional.getCode());
            node.put("name", regional.getName());
            node.put("code", regional.getCode());
            node.put("children", new ArrayList<>());
            nodeMap.put(regional.getCode(), node);
        }
        System.out.println("创建节点完成：" + nodeMap.size() + " 个节点，耗时：" + (System.currentTimeMillis() - startTime) + "ms");
        
        // 构建父子关系
        int rootCount = 0;
        int childCount = 0;
        for (Regional regional : regionalList) {
            Map<String, Object> node = nodeMap.get(regional.getCode());
            String parentCode = regional.getParent();
            
            if (parentCode == null || parentCode.isEmpty() || "0".equals(parentCode)) {
                // 根节点
                tree.add(node);
                rootCount++;
            } else {
                // 子节点
                Map<String, Object> parentNode = nodeMap.get(parentCode);
                if (parentNode != null) {
                    List<Map<String, Object>> children = (List<Map<String, Object>>) parentNode.get("children");
                    children.add(node);
                    childCount++;
                }
            }
        }
        System.out.println("构建父子关系完成：根节点 " + rootCount + " 个，子节点 " + childCount + " 个，耗时：" + (System.currentTimeMillis() - startTime) + "ms");
        System.out.println("树形结构构建完成：" + tree.size() + " 个根节点，总耗时：" + (System.currentTimeMillis() - startTime) + "ms");
        
        return tree;
    }
}