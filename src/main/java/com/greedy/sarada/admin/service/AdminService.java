package com.greedy.sarada.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.greedy.sarada.admin.dao.AdminMapper;
import com.greedy.sarada.common.paging.Pagenation;
import com.greedy.sarada.common.paging.SelectCriteria;
import com.greedy.sarada.sell.dto.SellDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AdminService {
	
	private final AdminMapper adminMapper;
	
	public AdminService(AdminMapper adminMapper) {
		this.adminMapper = adminMapper;
	}
	
	public List<SellDto> findSellRegistList() {
		
		return adminMapper.findSellRegistList();
	}

	public Map<String, Object> selectSellList(Map<String, String> searchMap, int page) {
		

		int totalCount = adminMapper.selectTotalCount(searchMap);
		log.info("[BoardService] totalCount : {}", totalCount);
		

		int limit = 10;

		int buttonAmount = 5;
		
		
		SelectCriteria selectCriteria = Pagenation.getSelectCriteria(page, totalCount, limit, buttonAmount, searchMap);
		log.info("[AdminService] selectCriteria : {}", selectCriteria);
		

		List<SellDto> sellList = adminMapper.selectSellList(selectCriteria);
		log.info("[BoardService] boardList : {}", sellList);
		
		Map<String, Object> boardListAndPaging = new HashMap<>();
		boardListAndPaging.put("paging", selectCriteria);
		boardListAndPaging.put("sellList", sellList);
		
		return boardListAndPaging;

	}

	public SellDto selectSellDetail(String sellNo) {
		
		return adminMapper.selectSellDetail(sellNo);
	}

	public int insertSeller(SellDto seller) {
		
		int result = adminMapper.insertSeller(seller);
		int result1 = adminMapper.insertAuthSell(seller);
		
		log.info("[insertSller] result{}", result);
		
		
		return (result == 1 && result1 == 1) ? 1 : 0;
	}

	public int rejectSeller(SellDto seller) {
		
		int result = adminMapper.rejectSeller(seller);
	
		log.info("[insertSller] result{}", result);
		
		return result;
	}



}
