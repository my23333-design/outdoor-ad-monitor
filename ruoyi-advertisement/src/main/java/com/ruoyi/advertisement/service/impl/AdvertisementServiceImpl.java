package com.ruoyi.advertisement.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.map.AddressComponent;
import com.ruoyi.common.utils.map.MapUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.advertisement.mapper.AdvertisementMapper;
import com.ruoyi.advertisement.domain.Advertisement;
import com.ruoyi.advertisement.service.IAdvertisementService;

/**
 * 广告列表Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-18
 */
@Service
public class AdvertisementServiceImpl implements IAdvertisementService 
{

    @Autowired
    private AdvertisementMapper advertisementMapper;

    @Autowired
    private MapUtils mapUtils;
    /**
     * 查询广告列表
     * 
     * @param id 广告列表主键
     * @return 广告列表
     */
    @Override
    public Advertisement selectAdvertisementById(Long id)
    {
        return advertisementMapper.selectAdvertisementById(id);
    }

    /**
     * 查询广告列表列表
     * 
     * @param advertisement 广告列表
     * @return 广告列表
     */
    @Override
    public List<Advertisement> selectAdvertisementList(Advertisement advertisement)
    {
        return advertisementMapper.selectAdvertisementList(advertisement);
    }

    /**
     * 新增广告列表
     * 
     * @param advertisement 广告列表
     * @return 结果
     */
    @Override
    public int insertAdvertisement(Advertisement advertisement)
    {
        System.out.println("=== 开始插入广告数据 ===");
        System.out.println("经纬度: " + advertisement.getLongitude() + ", " + advertisement.getLatitude());
        System.out.println("详细地址: " + advertisement.getAddress());
        
        AddressComponent addressComponent = mapUtils.findAddressByLngLat(advertisement.getLongitude(), advertisement.getLatitude());
        System.out.println("地址解析结果: " + (addressComponent != null ? addressComponent.toString() : "null"));
        
        if (addressComponent != null) {
            if (advertisement.getProvince() == null || advertisement.getProvince().isEmpty()) {
                advertisement.setProvince(addressComponent.getProvince());
                System.out.println("设置省份: " + addressComponent.getProvince());
            }
            if (advertisement.getCity() == null || advertisement.getCity().isEmpty()) {
                advertisement.setCity(addressComponent.getCity());
                System.out.println("设置城市: " + addressComponent.getCity());
            }
            if (advertisement.getDistrict() == null || advertisement.getDistrict().isEmpty()) {
                advertisement.setDistrict(addressComponent.getDistrict());
                System.out.println("设置区县: " + addressComponent.getDistrict());
            }
            if (advertisement.getStreet() == null || advertisement.getStreet().isEmpty()) {
                advertisement.setStreet(addressComponent.getTownship());
                System.out.println("设置街道: " + addressComponent.getTownship());
            }
        } else {
            System.out.println("地址解析失败，尝试从详细地址中提取...");
            extractAddressFromFullAddress(advertisement);
        }
        
        if (advertisement.getViolationType() == null || advertisement.getViolationType().isEmpty()) {
            advertisement.setViolationType("未分类");
        }
        advertisement.setAuditStatus("0");
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
        
        advertisement.setCreateTime(DateUtils.getNowDate());
        return advertisementMapper.insertAdvertisement(advertisement);
    }
    
    private void extractAddressFromFullAddress(Advertisement advertisement) {
        String fullAddress = advertisement.getAddress();
        if (fullAddress == null || fullAddress.isEmpty()) {
            return;
        }
        
        // 提取省份
        String[] provinces = {"北京市", "天津市", "河北省", "山西省", "内蒙古自治区", "辽宁省", "吉林省", "黑龙江省",
            "上海市", "江苏省", "浙江省", "安徽省", "福建省", "江西省", "山东省", "河南省", "湖北省", "湖南省",
            "广东省", "广西壮族自治区", "海南省", "重庆市", "四川省", "贵州省", "云南省", "西藏自治区",
            "陕西省", "甘肃省", "青海省", "宁夏回族自治区", "新疆维吾尔自治区", "香港特别行政区", "澳门特别行政区", "台湾省"};
        
        for (String province : provinces) {
            if (fullAddress.contains(province)) {
                if (advertisement.getProvince() == null || advertisement.getProvince().isEmpty()) {
                    advertisement.setProvince(province);
                    System.out.println("从地址提取省份: " + province);
                }
                break;
            }
        }
        
        // 提取城市（简单处理，匹配常见城市名）
        if (advertisement.getCity() == null || advertisement.getCity().isEmpty()) {
            int idx = fullAddress.indexOf("市");
            if (idx > 0) {
                int startIdx = idx - 3;
                if (startIdx < 0) startIdx = 0;
                String city = fullAddress.substring(startIdx, idx + 1);
                // 确保城市名合理
                if (city.length() >= 2 && city.length() <= 4) {
                    advertisement.setCity(city);
                    System.out.println("从地址提取城市: " + city);
                }
            }
        }
        
        // 提取区县
        if (advertisement.getDistrict() == null || advertisement.getDistrict().isEmpty()) {
            int idx = fullAddress.indexOf("区");
            if (idx > 0) {
                int startIdx = idx - 3;
                if (startIdx < 0) startIdx = 0;
                String district = fullAddress.substring(startIdx, idx + 1);
                if (district.length() >= 2 && district.length() <= 4) {
                    advertisement.setDistrict(district);
                    System.out.println("从地址提取区县: " + district);
                }
            }
        }
        
        // 提取街道
        if (advertisement.getStreet() == null || advertisement.getStreet().isEmpty()) {
            int idx = fullAddress.indexOf("街");
            if (idx > 0) {
                int startIdx = idx - 4;
                if (startIdx < 0) startIdx = 0;
                String street = fullAddress.substring(startIdx, idx + 1);
                if (street.length() >= 2) {
                    advertisement.setStreet(street);
                    System.out.println("从地址提取街道: " + street);
                }
            }
        }
    }

    /**
     * 修改广告列表
     * 
     * @param advertisement 广告列表
     * @return 结果
     */
    @Override
    public int updateAdvertisement(Advertisement advertisement)
    {
        advertisement.setUpdateTime(DateUtils.getNowDate());
        return advertisementMapper.updateAdvertisement(advertisement);
    }

    /**
     * 批量删除广告列表
     * 
     * @param ids 需要删除的广告列表主键
     * @return 结果
     */
    @Override
    public int deleteAdvertisementByIds(Long[] ids)
    {
        return advertisementMapper.deleteAdvertisementByIds(ids);
    }

    /**
     * 删除广告列表信息
     * 
     * @param id 广告列表主键
     * @return 结果
     */
    @Override
    public int deleteAdvertisementById(Long id)
    {
        return advertisementMapper.deleteAdvertisementById(id);
    }

    @Override
    public int resetAllAdvertisementStatus()
    {
        return advertisementMapper.resetAllAdvertisementStatus();
    }

    public void automatedReview() {
    }
}
