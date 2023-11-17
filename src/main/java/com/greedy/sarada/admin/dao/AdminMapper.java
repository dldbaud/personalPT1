package com.greedy.sarada.admin.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.greedy.sarada.common.paging.SelectCriteria;
import com.greedy.sarada.sell.dto.SellDto;

@Mapper
public interface AdminMapper {

	List<SellDto> findSellRegistList();

	int selectTotalCount(Map<String, String> searchMap);

	List<SellDto> selectSellList(SelectCriteria selectCriteria);

}
