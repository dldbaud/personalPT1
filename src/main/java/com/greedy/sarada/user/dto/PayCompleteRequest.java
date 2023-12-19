package com.greedy.sarada.user.dto;

import java.util.List;

import com.greedy.sarada.sell.dto.PtDto;

import lombok.Data;

@Data
public class PayCompleteRequest {
	
	private List<PtDto> ptList;
	private String orderNo;
    private String payNo;
    private String listNo;
    private String listNm;
}
