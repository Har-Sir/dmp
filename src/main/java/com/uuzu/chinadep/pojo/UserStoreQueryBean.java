package com.uuzu.chinadep.pojo;

import lombok.Data;

/**
 * 
 * @author jiangll
 *
 */
@Data
public class UserStoreQueryBean extends QueryBean {
	private String account;
	private String userid;
	private String storeid;
	private String name;
	private String country;
	private String province;
	private String city;
	private String area;
	private String address;
	private String lat;
	private String lon;
	private String type;
}
