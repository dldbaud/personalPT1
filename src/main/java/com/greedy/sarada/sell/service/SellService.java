package com.greedy.sarada.sell.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.greedy.sarada.common.exception.sell.SellRegistException;
import com.greedy.sarada.sell.dao.SellMapper;
import com.greedy.sarada.sell.dto.CategoryDto;
import com.greedy.sarada.sell.dto.FileDto;
import com.greedy.sarada.sell.dto.ListDto;
import com.greedy.sarada.sell.dto.PtDto;
import com.greedy.sarada.sell.dto.RefCategoryDto;
import com.greedy.sarada.sell.dto.SellDto;
import com.greedy.sarada.user.dto.UserDto;

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
	public void insertProdectFileList(ListDto list) {
			
		/* 2. file 테이블에 데이터 저장(첨부된 파일만큼) */
		for(FileDto file : list.getFileImageList()) {
			sellMapper.insertImage(file);
		}
		
	}
	
	public void registSellMainImage(ListDto list) {
		
		
		sellMapper.insertSellMainImage(list.getFileMain());
		
	}
	public void insertPt(PtDto pt) {
		
		sellMapper.insertPt(pt);
	}
	public SellDto selectSeller(UserDto user) {
		
		return sellMapper.selectSeller(user);
	}
	public void insertList(ListDto list) {
		
		/* 1. list 테이블에 데이터 저장 */
		sellMapper.insertSellList(list);
		
	}
	public List<CategoryDto> findAllCategoryList(String categoryCode) {
		// TODO Auto-generated method stub
		return sellMapper.findAllCategoryList(categoryCode);
	}
	public Map<String, Object> findCategory() {
		
		List<RefCategoryDto> refCategory = sellMapper.findAllRefCategory();
		List<CategoryDto> category = sellMapper.findAllCategory();
		
		Map<String, Object> categoryList = new HashMap<>();
		
		categoryList.put("refCategory", refCategory);
		categoryList.put("category", category);
		
		return categoryList;
	}
	public Map<String, Object> productDetails(String listNo) {
		
		ListDto selectList = sellMapper.selectList(listNo);
		List<PtDto> selectPtList = sellMapper.selectPtList(listNo);
		List<FileDto> selectFileList = sellMapper.selectFileList(listNo);
		Map<String, Object> productDetiis = new HashMap<>();
		productDetiis.put("sList", selectList);
		productDetiis.put("sPtList", selectPtList);
		productDetiis.put("sFilelist", selectFileList);
		
		return productDetiis;
	}
	public SellDto findSellRegist(UserDto user) {
		
		return sellMapper.findSellRegist(user);
	}
	public List<PtDto> manageProducts(UserDto user) {
		
		return sellMapper.selectPtManageList(user);
	}
	public PtDto ptManage(String ptNo, String sellNo) {
		// TODO Auto-generated method stub
		return sellMapper.ptManage(ptNo, sellNo);
	}
	public void ptUpdate(PtDto pt) {
		
		int result = sellMapper.ptUpdate(pt);
		
	}
	
}
