package com.freedy.backend.entity.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Freedy
 * @date 2021/5/19 0:58
 */
@Data
public class IpRegionDto implements Serializable {
    private String area;
    private String country;
    private String isp_id;
    private String queryIp;
    private String city;
    private String ip;
    private String isp;
    private String county;
    private String region_id;
    private String area_id;
    private Object county_id;
    private String region;
    private String country_id;
    private String city_id;
}
