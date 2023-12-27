package com.greedy.sarada.user.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.greedy.sarada.common.paging.SelectCriteria;
import com.greedy.sarada.provider.SnsDto;
import com.greedy.sarada.user.dto.OrderDto;
import com.greedy.sarada.user.dto.OrderItemDto;
import com.greedy.sarada.user.dto.PayDto;
import com.greedy.sarada.user.dto.RefundDto;
import com.greedy.sarada.user.dto.ReplyDto;
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

	List<OrderDto> SelectOrderLists(String userNo);

	int selectOrderReadyCounting(String userNo);
	
	//맵과 함꼐 여러 인자를 넘기는경우 또는 인자를 2개이상 넘기는 데 데이터 타입이 다를 경우 xml 작성 중요
	int selectOrderCount(@Param("userNo") String userNo, @Param("searchMap") Map<String, String> searchMap);

	List<OrderDto> selectOrderList(@Param("selectCriteria") SelectCriteria selectCriteria, @Param("userNo") String userNo);

	OrderDto selectOrderDetail(String orderNo, String userNo);

	String findPayNo(String orderNo);

	int inserRefund(RefundDto refund);

	String findListNm(String orderNo);

	int replyInsert(ReplyDto reply);

	int selectReplyTotalCount(String refListNo);
	
	List<ReplyDto> selectReplyList(SelectCriteria selectCriteria);

	ReplyDto replyCheck(@Param("user") UserDto user, @Param("listNo") String listNo);

	int replyUpdate(ReplyDto reply);

	OrderDto orderCheck(OrderDto order);


	


//	String findByEmail(String email);

//	int insertEmail(String email);

}