package com.greedy.sarada.provider;

import com.greedy.sarada.user.dto.UserDto;

import lombok.Data;

@Data
public class SnsDto {
	
	private String snsNo;
	private String snsId;
//	private String userNo;
	private UserDto user;
	private String snsNm;
}
