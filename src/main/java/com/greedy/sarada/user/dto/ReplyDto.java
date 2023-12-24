package com.greedy.sarada.user.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class ReplyDto {
	
	private int replyNo;
	private String refListNo;
	private String replyBody;
	private UserDto writer;
	private String replyStatus;
	private Date replyDate;

}
