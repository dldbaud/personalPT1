package com.greedy.sarada.sell.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.greedy.sarada.sell.dto.CategoryDto;
import com.greedy.sarada.sell.dto.RefCategoryDto;
import com.greedy.sarada.sell.dto.SellDto;

@Mapper
public interface SellMapper {

	
	List<CategoryDto> findAllCategory();

	List<RefCategoryDto> findAllRefCategory();

	int sellRegist(SellDto sellUser);

	int insertSellUserRole();

}
