package com.greedy.sarada.user.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.greedy.sarada.SaradaApplication;
import com.greedy.sarada.common.exception.user.MemberRegistException;
import com.greedy.sarada.sell.dao.SellMapper;
import com.greedy.sarada.sell.dto.SellDto;
import com.greedy.sarada.user.dto.UserDto;

@SpringBootTest
@ContextConfiguration(classes = {SaradaApplication.class})
public class UserMapperTests {
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private SellMapper sellMapper;
	
	@Test
	@DisplayName("회원가입 매퍼 인터페이스 메소드 확인")
	public void testUserRegist() {
		
		//given
		UserDto user = new UserDto();
		user.setId("테스트");
		user.setPhone("123456");
		user.setAddress("성" + "$" + "공성" + "$" + "공");
		user.setUserNm("테스트이름");
		user.setPwd("123");
		
		//when
		int result = userMapper.insertUser(user);
		
		//then
		assertEquals(1, result);
	}
	
	@Test
	@DisplayName("회원 정보 수정이 잘 되었는지 매퍼 인터페이스의 메소드 확인")
	public void testUpdateUser() {
		
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
	
	@Test
	@DisplayName("판매자 등록이 잘 되었는지 매퍼 인터페이스의 메소드 확인")
	public void testInsertSell() {
		
		// given
		SellDto sell = new SellDto();
		UserDto user = new UserDto();
		user.setUserNo("US56");
		sell.setSellAddress("123");
		sell.setSellStatus("대기");
		sell.setSellNo("SE12");
		sell.setSellerNo("US56");
		sell.setSellNm("판매자서비스테스트");
		sell.setSellCategoryNm("음식");
		sell.setSellPhone("987654321");
		sell.setSellType("개인");
		sell.setUser(user);
		//when
		int result = sellMapper.sellRegist(sell);
		
		//then
		assertEquals(1, result);
	}
}
