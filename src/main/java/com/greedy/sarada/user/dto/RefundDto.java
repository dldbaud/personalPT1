package com.greedy.sarada.user.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class RefundDto {
	
	private String refundNo;
	private String orderNo;
	private int refundPrice;
	private Date refundDate;
	private String refundStatus;
	private String listNo;
	
}
