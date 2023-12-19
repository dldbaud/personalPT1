package com.greedy.sarada.user.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.greedy.sarada.provider.SnsDto;
import com.greedy.sarada.user.dto.OrderDto;
import com.greedy.sarada.user.dto.OrderItemDto;
import com.greedy.sarada.user.dto.PayDto;
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

	String findByUserById(String userNm, String phone);

	int insertOrderItem(OrderItemDto orderItemDto);

	int insertOrder(OrderDto order);

	int insertPay(PayDto pay);


//	String findByEmail(String email);

//	int insertEmail(String email);



}
