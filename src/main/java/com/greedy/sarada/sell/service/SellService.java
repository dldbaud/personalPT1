package com.greedy.sarada.sell.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.greedy.comprehensive.board.dto.AttachmentDTO;
import com.greedy.sarada.common.exception.sell.SellRegistException;
import com.greedy.sarada.sell.dao.SellMapper;
import com.greedy.sarada.sell.dto.CategoryDto;
import com.greedy.sarada.sell.dto.FileDto;
import com.greedy.sarada.sell.dto.ListDto;
import com.greedy.sarada.sell.dto.PtDto;
import com.greedy.sarada.sell.dto.RefCategoryDto;
import com.greedy.sarada.sell.dto.SellDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class SellService {
	
	private final SellMapper sellMapper;
	
	public SellService(SellMapper sellMapper) {
		this.sellMapper = sellMapper;
	}
	public List<CategoryDto> findAllCategory() {
		
		return sellMapper.findAllCategory();
		
	}
	public List<RefCategoryDto> findAllRefCategory() {
		
		return sellMapper.findAllRefCategory();
	}
	public void registUser(SellDto sellUser) throws SellRegistException {
		
		int result1 = sellMapper.sellRegist(sellUser);
		
	    if(!(result1 > 0 )){
	            throw new SellRegistException("판매자 신청에 실패하였습니다.");
	        }
		
	}
	public void registSellList(ListDto list) {
			
		/* 2. file 테이블에 데이터 저장(첨부된 파일만큼) */
		for(FileDto file : list.getFileImageList()) {
			sellMapper.insertImage(file);
		}
		
	}
	
	public void registSellMainImage(ListDto list) {
		
		/* 1. list 테이블에 데이터 저장 */
		sellMapper.insertSellList(list);
		
		sellMapper.insertSellMainImage(list.getFileMain());
		
	}
	public void insertPt(PtDto pt) {
		
		sellMapper.insertPt(pt);
	}
	
}
