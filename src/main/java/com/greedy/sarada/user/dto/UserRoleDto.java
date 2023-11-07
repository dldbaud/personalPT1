package com.greedy.sarada.user.dto;

import lombok.Data;

@Data
public class UserRoleDto {
	
	private String authNo;
	private String userNo;
	
	/* TB_AUTH - 권한 코드별로 가지는 권한을 나타냄 */
	private AuthDto auth;
	
}
