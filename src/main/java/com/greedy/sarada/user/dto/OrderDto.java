package com.greedy.sarada.user.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class OrderDto {

	private String orderNo;
	private int orderPrice;
	private String listNm;
	private String deliveryStatus;
	private Date orderDate;
	private String userNo;
	private String listNo;
	
}
