package com.greedy.sarada.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.greedy.sarada.admin.dao.AdminMapper;
import com.greedy.sarada.common.paging.Pagenation;
import com.greedy.sarada.common.paging.SelectCriteria;
import com.greedy.sarada.sell.dto.ListDto;
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
		

		int totalCount = adminMapper.selectSellTotalCount(searchMap);
		log.info("[BoardService] totalCount : {}", totalCount);
		

		int limit = 10;

		int buttonAmount = 5;
		
		
		SelectCriteria selectCriteria = Pagenation.getSelectCriteria(page, totalCount, limit, buttonAmount, searchMap);
		log.info("[AdminService] selectCriteria : {}", selectCriteria);
		

		List<SellDto> sellList = adminMapper.selectSellList(selectCriteria);
		log.info("[AdminService] boardList : {}", sellList);
		
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

	public Map<String, Object> findListViewPageing(int currentPage, Map<String, String> searchMap) {
		
		/* 1. 전체 게시글 수 확인 (검색어가 있는 경우 포함) => 페이징 처리 계산을 위해서 */
		int totalCount = adminMapper.selectListTotalCount(searchMap);
		log.info("[AdminService] totalCount : {}", totalCount);
		
		/* 한 페이지에 보여줄 게시물의 수 */
		int limit = 8;
		/* 한 번에 보여질 페이징 버튼의 수 */
		int buttonAmount = 5;
		
		/* 2. 페이징 처리와 연관 된 값을 계산하여 SelectCriteria 타입의 객체에 담는다. */
		SelectCriteria selectCriteria = Pagenation.getSelectCriteria(currentPage, totalCount, limit, buttonAmount, searchMap);
		log.info("[AdminService] selectCriteria : {}", selectCriteria);
		
		/* 3. 요청 페이지와 검색 기준에 맞는 게시글을 조회해온다. */
		List<ListDto> boardList = adminMapper.selectList(selectCriteria);
		log.info("[AdminService] boardList : {}", boardList);
		
		Map<String, Object> boardListAndPaging = new HashMap<>();
		boardListAndPaging.put("paging", selectCriteria);
		boardListAndPaging.put("boardList", boardList);
		
		return boardListAndPaging;
	}

}
