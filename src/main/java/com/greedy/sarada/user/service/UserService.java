package com.greedy.sarada.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.greedy.sarada.common.exception.user.InsertReplyException;
import com.greedy.sarada.common.exception.user.MemberModifyException;
import com.greedy.sarada.common.exception.user.MemberRegistException;
import com.greedy.sarada.common.exception.user.insertOrderException;
import com.greedy.sarada.common.exception.user.insertOrderItemException;
import com.greedy.sarada.common.exception.user.insertPayException;
import com.greedy.sarada.common.paging.Pagenation;
import com.greedy.sarada.common.paging.SelectCriteria;
import com.greedy.sarada.provider.SnsDto;
import com.greedy.sarada.sell.dto.ListDto;
import com.greedy.sarada.sell.dto.PtDto;
import com.greedy.sarada.user.dao.UserMapper;
import com.greedy.sarada.user.dto.OrderDto;
import com.greedy.sarada.user.dto.OrderItemDto;
import com.greedy.sarada.user.dto.PayCompleteRequest;
import com.greedy.sarada.user.dto.PayDto;
import com.greedy.sarada.user.dto.RefundDto;
import com.greedy.sarada.user.dto.UserDto;
import com.siot.IamportRestClient.response.Payment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class UserService {
	
    private final UserMapper mapper;

    public UserService(UserMapper mapper) {
        this.mapper = mapper;
    }

	public void registUser(UserDto user) throws MemberRegistException {
		
		int result1 = mapper.insertUser(user);
		int result2 = mapper.insertUserRole();
		
	    if(!(result1 > 0 && result2 > 0)){
	            throw new MemberRegistException("회원 가입에 실패하였습니다.");
	        }
	}


	
	public boolean selectUserById(String id) {
		
		String result = mapper.selectById(id);
		
		return result != null ? true : false;
	}

	public void registSnsUser(UserDto user, SnsDto sns) throws MemberRegistException {
		
		int result1 = mapper.insertUser(user);
		int result3 = mapper.insertSns(sns);
		int result2 = mapper.insertUserRole();
		
	    if(!(result1 > 0 && result2 > 0)){
	            throw new MemberRegistException("회원 가입에 실패하였습니다.");
	        }
		
	}

	public boolean selectUserProviderId(String userName) {
		
		String result = mapper.selectByProviderId(userName);
		log.info("프로바이더 확인 : " + userName);
		return result == null ? true : false;
	}

	public void modifyUser(UserDto user) throws MemberModifyException {
		
		int result = mapper.modifyUser(user);
		
		if(!(result>0)) {
			 throw new MemberModifyException("회원 정보 수정에 실패하셨습니다.");
		}
		
	}


	public String findUserById(String userNm, String phone) {
		
		return mapper.findByUserById(userNm,phone);
	
	}

	public void insertOrder(PayCompleteRequest request, Payment paymentResult, UserDto loginUser) throws insertOrderItemException, insertOrderException, insertPayException {
		
		log.info("insertOrder 확인 : ");
		OrderDto order = new OrderDto();
		order.setOrderNo(request.getOrderNo());
		order.setOrderPrice(paymentResult.getAmount().intValue());
		order.setListNm(request.getListNm());
		order.setDeliveryStatus("준비중");
		order.setListNo(request.getListNo());
		order.setUserNo(loginUser.getUserNo());
		
		int result1 = mapper.insertOrder(order);
		if(!(result1 > 0)) {
			throw new insertOrderException("order테이블 삽입 실패");
		}
		
		for(PtDto pt : request.getPtList()) {
			log.info("insertOrderItem 확인 : ");
			OrderItemDto orderItemDto = new OrderItemDto();
			orderItemDto.setOrderNo(request.getOrderNo());
			orderItemDto.setPtNm(pt.getPtNm());
			orderItemDto.setPtNo(pt.getPtNo());
			orderItemDto.setOrderCount(pt.getStCount());
			
			int rsesult = mapper.insertOrderItem(orderItemDto);
			
			if(!( rsesult > 0)) {
				throw new insertOrderItemException("orderItem 테이블 삽입 실패"); 
			}
		}
		
		PayDto pay = new PayDto();
		pay.setPayNo(request.getPayNo());
		pay.setOrderNo(request.getOrderNo());
		pay.setPayMethod(paymentResult.getPayMethod());
		pay.setPayPrice(paymentResult.getAmount().intValue());
		pay.setUserNo(loginUser.getUserNo());
		
		int result2 = mapper.insertPay(pay);
		
		if(!( result2 > 0)) {
			throw new insertPayException("pay 테이블 삽입 실패"); 
		}
	}

	public Map<String, Object> selectUserInfo(String id) {
		
		UserDto user = mapper.findByUserId(id);
		Map<String, Object> userInfo = new HashMap<>();
		
		userInfo.put("userInfo", user);
		
		return userInfo;
	}

	public Map<String, Object> selectOrderList(String userNo) {
		
		List<OrderDto> orderList = mapper.SelectOrderLists(userNo);
		
		Map<String, Object> orderLists = new HashMap<>();
		orderLists.put("orderLists", orderList);
		return orderLists;
	}

	public int orderReadyCounting(String userNo) {
		
		int orderReadyCounting = mapper.selectOrderReadyCounting(userNo);
		
		return orderReadyCounting;
	}

	public Map<String, Object> findOrderListViewPaging(int page, Map<String, String> searchMap, String userNo) {
		
		int orderCount = mapper.selectOrderCount(userNo, searchMap);
		
		/* 한 페이지에 보여줄 게시물의 수 */
		int limit = 8;
		/* 한 번에 보여질 페이징 버튼의 수 */
		int buttonAmount = 5;
		
		SelectCriteria selectCriteria = Pagenation.getSelectCriteria(page, orderCount, limit, buttonAmount, searchMap);
		log.info("[UserService] selectCriteria : {}", selectCriteria);
		
		/* 3. 요청 페이지와 검색 기준에 맞는 게시글을 조회해온다. */
		List<OrderDto> orderList = mapper.selectOrderList(selectCriteria, userNo);
		log.info("[UserService] boardList : {}", orderList);
		
		Map<String, Object> boardListAndPaging = new HashMap<>();
		boardListAndPaging.put("paging", selectCriteria);
		boardListAndPaging.put("orderList", orderList);
		return boardListAndPaging;
	}

	public OrderDto selectOrderDetail(String orderNo, String userNo) {
		
		log.info("[UserService] selectOrderDetail : {}", "시작");
		return mapper.selectOrderDetail(orderNo, userNo);
	}

	public String findPayNo(String orderNo) {
		
		log.info("[UserService] findPayNo {}", orderNo);
		
		return mapper.findPayNo(orderNo);
	}

	public void insertRefund(RefundDto refund) {
		
		int result = mapper.inserRefund(refund);
		
	}

	public String findListNm(String orderNo) {
		
		log.info("[UserService] findListNm {}", orderNo);
		
		return mapper.findListNm(orderNo);
	}

	public void replyInsert(String userNo, String replyBody) throws InsertReplyException {
		
		int result = mapper.replyInsert(userNo, replyBody);
		
		if(!(result > 0)) {
			throw new InsertReplyException("댓글 작성 실패"); 
		}
		
	}

	
}
