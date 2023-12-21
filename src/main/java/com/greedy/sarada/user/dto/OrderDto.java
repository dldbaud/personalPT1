package com.greedy.sarada.user.dto;

import java.sql.Date;
import java.util.List;

import com.greedy.sarada.sell.dto.FileDto;

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
	
	private FileDto mainFile;
	private List<FileDto> fileList;
	private List<OrderItemDto> orderItem;
}
