package com.greedy.sarada.user.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.greedy.sarada.SaradaApplication;
import com.greedy.sarada.user.dto.UserDto;

@SpringBootTest
@ContextConfiguration(classes = {SaradaApplication.class})
public class UserMapperTests {
	
	@Autowired
	private UserMapper userMapper;
	
	@Test
	@DisplayName("회원 정보 수정이 잘 되었는지 매퍼 인터페이스의 메소드 확인")
	public void testRegistMenu() {
		
		//given
		UserDto user = new UserDto();
		user.setId("asd");
		user.setAddress("테스트");
		user.setPhone("전화테스트");
		user.setUserStatus("Y");
		//when
		int result = userMapper.modifyUser(user);
		
		//then
		assertEquals(1, result);
	}

}
