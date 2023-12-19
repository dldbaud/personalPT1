package com.greedy.sarada.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.greedy.sarada.common.exception.user.MemberModifyException;
import com.greedy.sarada.common.exception.user.MemberRegistException;
import com.greedy.sarada.common.exception.user.insertOrderException;
import com.greedy.sarada.common.exception.user.insertOrderItemException;
import com.greedy.sarada.common.exception.user.insertPayException;
import com.greedy.sarada.provider.SnsDto;
import com.greedy.sarada.sell.dto.PtDto;
import com.greedy.sarada.user.dao.UserMapper;
import com.greedy.sarada.user.dto.OrderDto;
import com.greedy.sarada.user.dto.OrderItemDto;
import com.greedy.sarada.user.dto.PayCompleteRequest;
import com.greedy.sarada.user.dto.PayDto;
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


	
}
