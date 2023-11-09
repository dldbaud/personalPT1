package com.greedy.sarada.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.greedy.sarada.common.exception.user.MemberModifyException;
import com.greedy.sarada.common.exception.user.MemberRegistException;
import com.greedy.sarada.provider.SnsDto;
import com.greedy.sarada.user.controller.UserController;
import com.greedy.sarada.user.dao.UserMapper;
import com.greedy.sarada.user.dto.UserDto;

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


	
}
