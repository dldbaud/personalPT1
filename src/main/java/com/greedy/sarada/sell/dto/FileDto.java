package com.greedy.sarada.sell.dto;

import lombok.Data;

@Data
public class FileDto {
	
	private String fileNo;
	private PtDto pt;
	private String fileType;
	private String originalFileNm;
	private String savedFileNm;
	private String filePath;
}
