package com.greedy.sarada.user.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class PayDto {
	
	private String payNo;
	private Date payDate;
	private String payMethod;
	private int payPrice;
	private String orderNo;
	private String userNo;

}
