package com.greedy.sarada.user.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.greedy.sarada.SaradaApplication;
import com.greedy.sarada.common.exception.sell.SellRegistException;
import com.greedy.sarada.common.exception.user.MemberModifyException;
import com.greedy.sarada.common.exception.user.MemberRegistException;
import com.greedy.sarada.sell.dto.SellDto;
import com.greedy.sarada.sell.service.SellService;
import com.greedy.sarada.user.dto.UserDto;

@SpringBootTest
@ContextConfiguration(classes = {SaradaApplication.class})
public class UserServiceTests {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SellService sellService;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Test
	public void 회원가입_서비스_테스트() {
		
		UserDto user = new UserDto();
		user.setId("테스트");
		user.setPhone("123456");
		user.setAddress("성" + "$" + "공성" + "$" + "공");
		user.setUserNm("테스트이름");
		user.setPwd("123");
		
		// when & then
		assertThrows(MemberRegistException.class, () -> {
			userService.registUser(user);
		});
		

	}
	
	@Test
	public void 회원정보_업데이트_서비스_성공_테스트() throws Exception {
		
		UserDto user = new UserDto();
		user.setId("asd");
		user.setAddress("성" + "$" + "공성" + "$" + "공");
		user.setPhone("888888");
		
	    // when & then
	    assertThrows(MemberModifyException.class, () -> {
	        userService.modifyUser(user);
	    });
	}
	
	@Test
	public void 회원정보_업데이트_서비스_실패_테스트() throws Exception {
		
		UserDto user = new UserDto();
		user.setAddress("성" + "&" + "공성" + "$" + "공");
		user.setPhone("888888");
		user.setUserStatus("Y");
	    // when & then
	    assertThrows(MemberModifyException.class, () -> {
	        userService.modifyUser(user);
	    });
	}
	
	@Test
	public void 판매자_등록_서비스_테스트() throws Exception {
		
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
		
		//when & then
		assertThrows(SellRegistException.class, () -> {
	        sellService.registUser(sell);
	    });
		
	}
}
