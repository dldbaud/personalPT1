package com.greedy.sarada.sell.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.greedy.sarada.common.exception.sell.SellRegistException;
import com.greedy.sarada.sell.dao.SellMapper;
import com.greedy.sarada.sell.dto.CategoryDto;
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
	
}
