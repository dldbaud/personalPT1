package com.greedy.sarada.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greedy.sarada.SaradaApplication;
import com.greedy.sarada.user.dao.UserMapper;
import com.greedy.sarada.user.dto.UserDto;
import com.greedy.sarada.user.service.AuthenticationService;
import com.greedy.sarada.user.service.UserService;

@SpringBootTest
@ContextConfiguration(classes = {SaradaApplication.class})
public class UserControllerTests {

	@Autowired
	private UserController userController;
	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private UserService userService;
	
	private MockMvc mockMvc;
	
	@Autowired
	private UserMapper userMapper;
	
	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
	}
	
	
	@Test
	public void 회원정보_삽입_컨트롤러_테스트() throws Exception {
		
		//given
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("id", "삽입테스트");
		params.add("pwd", "123");
		params.add("phone", "01235678");
		params.add("userNm", "삽입테스트이름");
		params.add("zipCode", "123");
		params.add("address1", "789");
		params.add("address2", "456");
		
		//when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/user/regist").params(params))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.flash().attributeCount(1))
				.andExpect(MockMvcResultMatchers.redirectedUrl("/"))
				.andDo(MockMvcResultHandlers.print());

	}
	
	@Test
	public void 회원정보_업데이트_컨트롤러_테스트() throws Exception {
		UserDetails existingUser = authenticationService.loadUserByUsername("asd");

		// @AuthenticationPrincipal로 주입
		Authentication authentication = new UsernamePasswordAuthenticationToken(existingUser, null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		// given
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("zipCode", "zipCodeValue");
		params.add("address1", "address1Value");
		params.add("address2", "address2Value");
		params.add("address", "address2Value");
		params.add("phone", "9999999");
		params.add("id", "asd");
		//when & then
				mockMvc.perform(MockMvcRequestBuilders.post("/user/update").params(params))
					.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
					.andExpect(MockMvcResultMatchers.flash().attributeCount(1))
					.andExpect(MockMvcResultMatchers.redirectedUrl("/"))
					.andDo(MockMvcResultHandlers.print());

	}
	
	@Test
	public void 판매자_등록_컨트롤러_테스트() throws Exception {
		
		UserDetails existingUser = authenticationService.loadUserByUsername("asd");

		// @AuthenticationPrincipal로 주입
		Authentication authentication = new UsernamePasswordAuthenticationToken(existingUser, null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		// given
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("zipCode", "123");
		params.add("address1", "789");
		params.add("address2", "456");
		params.add("status", "대기");
		params.add("sellNo", "SE12");
		params.add("userNo", "US56");
		params.add("sellNm", "판매자삽입테스트");
		params.add("sellCategoryNm", "음식");
		params.add("sellPhone", "987654321");
		params.add("sellType", "개인");
		params.add("sellerNo", "US56");
		
		
		//when & then
		mockMvc.perform(MockMvcRequestBuilders.post("/user/sellRegist").params(params))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.flash().attributeCount(1))
			.andExpect(MockMvcResultMatchers.redirectedUrl("/"))
			.andDo(MockMvcResultHandlers.print());
		
	}
}
