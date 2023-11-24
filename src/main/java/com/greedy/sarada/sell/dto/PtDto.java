package com.greedy.sarada.sell.dto;

import java.util.List;

import com.greedy.sarada.user.dto.UserDto;

import lombok.Data;

@Data
public class PtDto {

	private String ptNo;
	private String ptNm;
	private int price;
	private int stCount;
	private UserDto user;
	private String ptDescrip;
	private String listNo;
	private List<FileDto> fileList;
}
