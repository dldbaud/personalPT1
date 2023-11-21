package com.greedy.sarada.sell.dto;

import java.sql.Date;

import com.greedy.sarada.user.dto.UserDto;

import lombok.Data;

@Data
public class SellDto {
	
	private String sellNo;
	private UserDto user;
	private String sellNm;
	private String sellAddress;
	private String sellCategoryNm;
	private String sellType;
	private String sellPhone;
	private String sellStatus;
	private Date sellReqDate;
	private Date sellRegiDate;
	private String sellerNo;
		
}
