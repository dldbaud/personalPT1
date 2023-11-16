package com.greedy.sarada.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.greedy.sarada.admin.dao.AdminMapper;
import com.greedy.sarada.common.paging.Pagenation;
import com.greedy.sarada.common.paging.SelectCriteria;
import com.greedy.sarada.sell.dto.SellDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdminService {
	
	private final AdminMapper adminMapper;
	
	public AdminService(AdminMapper adminMapper) {
		this.adminMapper = adminMapper;
	}
	
	public List<SellDto> findSellRegistList() {
		
		return adminMapper.findSellRegistList();
	}

	public Map<String, Object> selectBoardList(Map<String, String> searchMap, int page) {
		
		/* 1. 전체 게시글 수 확인 (검색어가 있는 경우 포함) => 페이징 처리 계산을 위해서 */
		int totalCount = adminMapper.selectTotalCount(searchMap);
		log.info("[BoardService] totalCount : {}", totalCount);
		
		/* 한 페이지에 보여줄 게시물의 수 */
		int limit = 10;
		/* 한 번에 보여질 페이징 버튼의 수 */
		int buttonAmount = 5;
		
		/* 2. 페이징 처리와 연관 된 값을 계산하여 SelectCriteria 타입의 객체에 담는다. */
		SelectCriteria selectCriteria = Pagenation.getSelectCriteria(page, totalCount, limit, buttonAmount, searchMap);
		log.info("[BoardService] selectCriteria : {}", selectCriteria);
		
		/* 3. 요청 페이지와 검색 기준에 맞는 게시글을 조회해온다. */
		List<SellDto> boardList = adminMapper.selectBoardList(selectCriteria);
		log.info("[BoardService] boardList : {}", boardList);
		
		Map<String, Object> boardListAndPaging = new HashMap<>();
		boardListAndPaging.put("paging", selectCriteria);
		boardListAndPaging.put("boardList", boardList);
		
		return boardListAndPaging;

	}

}
