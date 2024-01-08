package com.greedy.sarada.user.dto;

import java.util.List;

import com.greedy.sarada.sell.dto.PtDto;

import lombok.Data;

@Data
public class OrderItemDto {
	
	private String orderItemNo;
	private String orderNo;
	private String ptNo;
	private int orderCount;
	private String ptNm;
	private String ptSize;
	private PtDto pt;
}
