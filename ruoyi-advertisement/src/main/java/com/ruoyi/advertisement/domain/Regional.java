package com.ruoyi.advertisement.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 行政区域管理对象 tb_regional
 * 
 * @author wanghao
 * @date 2025-12-04
 */
public class Regional extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 地址编码 */
    private String code;

    /** 地区名称 */
    @Excel(name = "地区名称")
    private String name;

    /** 地区全称 */
    @Excel(name = "地区全称")
    private String sname;

    /** 父编码 */
    private String parent;

    /** 默认值 */
    private String initialization;

    /** 首字母 */
    @Excel(name = "首字母")
    private String sepll;

    /** 类型 */
    @Excel(name = "类型")
    private Long type;

    /** 排序 */
    private Long order;

    /** 等级 */
    @Excel(name = "等级")
    private Long level;

    /** 城乡分类代码 */
    private String villageType;

    /** 所属国家名 */
    @Excel(name = "所属国家名")
    private String nationName;

    /** 所属省名称 */
    @Excel(name = "所属省名称")
    private String provinceName;

    /** 所属市名称 */
    @Excel(name = "所属市名称")
    private String cityName;

    /** 所属区县名称 */
    @Excel(name = "所属区县名称")
    private String countyName;

    /** 所属街道名称 */
    @Excel(name = "所属街道名称")
    private String townName;

    /** 经度 */
    @Excel(name = "经度")
    private String lng;

    /** 纬度 */
    @Excel(name = "纬度")
    private String lat;

    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setSname(String sname) 
    {
        this.sname = sname;
    }

    public String getSname() 
    {
        return sname;
    }

    public void setParent(String parent) 
    {
        this.parent = parent;
    }

    public String getParent() 
    {
        return parent;
    }

    public void setInitialization(String initialization) 
    {
        this.initialization = initialization;
    }

    public String getInitialization() 
    {
        return initialization;
    }

    public void setSepll(String sepll) 
    {
        this.sepll = sepll;
    }

    public String getSepll() 
    {
        return sepll;
    }

    public void setType(Long type) 
    {
        this.type = type;
    }

    public Long getType() 
    {
        return type;
    }

    public void setOrder(Long order) 
    {
        this.order = order;
    }

    public Long getOrder() 
    {
        return order;
    }

    public void setLevel(Long level) 
    {
        this.level = level;
    }

    public Long getLevel() 
    {
        return level;
    }

    public void setVillageType(String villageType) 
    {
        this.villageType = villageType;
    }

    public String getVillageType() 
    {
        return villageType;
    }

    public void setNationName(String nationName) 
    {
        this.nationName = nationName;
    }

    public String getNationName() 
    {
        return nationName;
    }

    public void setProvinceName(String provinceName) 
    {
        this.provinceName = provinceName;
    }

    public String getProvinceName() 
    {
        return provinceName;
    }

    public void setCityName(String cityName) 
    {
        this.cityName = cityName;
    }

    public String getCityName() 
    {
        return cityName;
    }

    public void setCountyName(String countyName) 
    {
        this.countyName = countyName;
    }

    public String getCountyName() 
    {
        return countyName;
    }

    public void setTownName(String townName) 
    {
        this.townName = townName;
    }

    public String getTownName() 
    {
        return townName;
    }

    public void setLng(String lng) 
    {
        this.lng = lng;
    }

    public String getLng() 
    {
        return lng;
    }

    public void setLat(String lat) 
    {
        this.lat = lat;
    }

    public String getLat() 
    {
        return lat;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("code", getCode())
            .append("name", getName())
            .append("sname", getSname())
            .append("parent", getParent())
            .append("initialization", getInitialization())
            .append("sepll", getSepll())
            .append("type", getType())
            .append("order", getOrder())
            .append("level", getLevel())
            .append("remark", getRemark())
            .append("villageType", getVillageType())
            .append("nationName", getNationName())
            .append("provinceName", getProvinceName())
            .append("cityName", getCityName())
            .append("countyName", getCountyName())
            .append("townName", getTownName())
            .append("lng", getLng())
            .append("lat", getLat())
            .toString();
    }
}
