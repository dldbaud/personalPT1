package com.greedy.sarada.user.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.greedy.sarada.SaradaApplication;
import com.greedy.sarada.common.exception.user.MemberModifyException;
import com.greedy.sarada.user.dto.UserDto;

@SpringBootTest
@ContextConfiguration(classes = {SaradaApplication.class})
public class UserServiceTests {
	
	@Autowired
	private UserService userService;
	
	@Test
	public void 회원정보_업데이트_서비스_성공_테스트() throws Exception {
		
		UserDto user = new UserDto();
		user.setAddress("성" + "$" + "공성" + "$" + "공");
		user.setPhone("888888");
		
	    // when & then
	    MemberModifyException exception = assertThrows(MemberModifyException.class, () -> {
	        userService.modifyUser(user);
	    });
	}
	
	@Test
	public void 회원정보_업데이트_서비스_실패_테스트() throws Exception {
		
		UserDto user = new UserDto();
		user.setId("US56");
		user.setAddress("성" + "$" + "공성" + "$" + "공");
		user.setPhone("888888");
		user.setUserStatus("Y");
	    // when & then
	    MemberModifyException exception = assertThrows(MemberModifyException.class, () -> {
	        userService.modifyUser(user);
	    });
	}
}
