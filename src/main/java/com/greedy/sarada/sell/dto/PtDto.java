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
	private SellDto sell;
	private String ptDescrip;
	private String listNo;
	private String ptSize;
	private String ptAuthor;
	private CategoryDto category;
	private List<FileDto> fileList;
}
