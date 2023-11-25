package com.greedy.sarada.sell.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.greedy.sarada.sell.dto.CategoryDto;
import com.greedy.sarada.sell.dto.FileDto;
import com.greedy.sarada.sell.dto.ListDto;
import com.greedy.sarada.sell.dto.PtDto;
import com.greedy.sarada.sell.dto.RefCategoryDto;
import com.greedy.sarada.sell.dto.SellDto;

@Mapper
public interface SellMapper {

	
	List<CategoryDto> findAllCategory();

	List<RefCategoryDto> findAllRefCategory();

	int sellRegist(SellDto sellUser);

	int insertSellUserRole();

	void insertSellList(ListDto list);

	void insertImage(FileDto fileList);

	void insertSellMainImage(FileDto fileMain);

	void insertPt(PtDto pt);

}
