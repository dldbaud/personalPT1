package com.greedy.sarada.admin.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.greedy.sarada.common.paging.SelectCriteria;
import com.greedy.sarada.sell.dto.ListDto;
import com.greedy.sarada.sell.dto.SellDto;

@Mapper
public interface AdminMapper {

	List<SellDto> findSellRegistList();

	int selectSellTotalCount(Map<String, String> searchMap);

	List<SellDto> selectSellList(SelectCriteria selectCriteria);

	SellDto selectSellDetail(String sellNo);

	int insertSeller(SellDto seller);

	int rejectSeller(SellDto seller);

	int insertAuthSell(SellDto seller);

	List<ListDto> selectList(SelectCriteria selectCriteria);

	int selectListTotalCount(Map<String, String> searchMap);

}
