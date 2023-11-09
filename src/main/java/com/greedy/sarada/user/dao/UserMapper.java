package com.greedy.sarada.user.dao;

import org.apache.ibatis.annotations.Mapper;

import com.greedy.sarada.provider.SnsDto;
import com.greedy.sarada.user.dto.UserDto;

@Mapper
public interface UserMapper {

	UserDto findByUserId(String id);

	int insertUser(UserDto user);

	int insertUserRole();

	String selectById(String id);

	int insertSns(SnsDto sns);

	String selectByProviderId(String id);

	int modifyUser(UserDto user);


//	String findByEmail(String email);

//	int insertEmail(String email);



}
