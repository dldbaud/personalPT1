package com.greedy.sarada.sell.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.greedy.sarada.sell.dto.CategoryDto;
import com.greedy.sarada.sell.dto.FileDto;
import com.greedy.sarada.sell.dto.ListDto;
import com.greedy.sarada.sell.dto.PtDto;
import com.greedy.sarada.sell.dto.RefCategoryDto;
import com.greedy.sarada.sell.dto.SellDto;
import com.greedy.sarada.user.dto.UserDto;

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

	SellDto selectSeller(UserDto user);

	List<CategoryDto> findAllCategoryList(String categoryCode);

	ListDto selectList(String listNo);

	List<PtDto> selectPtList(String listNo);

	List<FileDto> selectFileList(String listNo);

	SellDto findSellRegist(UserDto user);

	List<PtDto> selectPtManageList(UserDto user);

	PtDto ptManage(String ptNo, String sellNo);

	int ptUpdate(PtDto pt);

//	List<PtDto> productDetail(String listNo);
//	List<ListDto> productDetail(String listNo);

}
