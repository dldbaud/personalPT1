package com.greedy.sarada.sell.dto;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.greedy.sarada.user.dto.UserDto;

import lombok.Data;

@Data
public class ListDto {
	
	private String listNo;
	private RefCategoryDto categoryCode;
	private String listNm;
	private Date registDate; 
	private String description;
	private UserDto sell;
	private FileDto fileMain;
	private List<PtDto> ptList;  //1:N
	private List<FileDto> fileImageList;
//	private Map<String, List<FileDto>> fileImageList;
}
