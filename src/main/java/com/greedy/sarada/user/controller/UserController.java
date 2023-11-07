package com.greedy.sarada.user.controller;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greedy.sarada.common.exception.user.MemberRegistException;
import com.greedy.sarada.user.dto.UserDto;
import com.greedy.sarada.user.service.AuthenticationService;
import com.greedy.sarada.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {
		
		private final PasswordEncoder passwordEncoder;
	    private final MessageSourceAccessor messageSourceAccessor;
	    private final UserService userService;
	    private final AuthenticationService authenticationService;
		
	    public UserController(PasswordEncoder passwordEncoder, MessageSourceAccessor messageSourceAccessor, UserService userService, AuthenticationService authenticationService) {
	    	this.passwordEncoder = passwordEncoder;
	    	this.messageSourceAccessor = messageSourceAccessor;
	    	this.userService = userService;
	    	this.authenticationService = authenticationService;
	    }
	    
	    @GetMapping("/test/login")
	    public @ResponseBody String Testlogin(Authentication authentication
	    		, @AuthenticationPrincipal UserDto userDetails
//	    		, @AuthenticationPrincipal UserDetails userDetails
	    		) {
	    	log.info("[UserController] Testlogin : " + authentication.getPrincipal());
	    	//sns으로 로그인하면 여기서 오류
	    	UserDto user =(UserDto) authentication.getPrincipal();
	    	log.info("[UserController] getPrincipal : " + user);
	    	
//	    	log.info("[UserController] userDeatis : " + userDetails.getUsername());
	    	log.info("[UserController] userDeatis : " + userDetails);
	    	return "세션 정보 확인하기";
	    }
	    
	    @GetMapping("/test/oauth/login")
	    public @ResponseBody String TestOAuthlogin(Authentication authentication
	    		,@AuthenticationPrincipal OAuth2User oAuth
	    		) {
	    	log.info("[UserController] TestOAuthlogin : " + authentication.getPrincipal());
	    	OAuth2User oAuth2User =(OAuth2User) authentication.getPrincipal();
	    	log.info("[UserController] oAuth2User : " + oAuth2User.getAttributes());
	    	log.info("[UserController] oAuth : " + oAuth.getAttributes());
	    	

	    	return "세션 정보 확인하기";
	    }
	    
	    /* 로그인 페이지 이동 */
	    @GetMapping("/login")
	    public String goLogin() {

	        return "user/login";
	    }
	    
	    /* 회원가입 페이지 이동 */
	    @GetMapping("/regist")
	    public String goRegist() {
	    	return "user/regist";
	    }
	    
	    /* 회원 가입 */
	    @PostMapping("/regist")
	    public String registMember(@ModelAttribute UserDto user,
	    		@RequestParam String zipCode, @RequestParam String address1, @RequestParam String address2,
	    		RedirectAttributes rttr) throws MemberRegistException {
	    	
	    	String address = zipCode + "$" + address1 + "$" + address2;
	    	user.setAddress(address);
	    	user.setPwd(passwordEncoder.encode(user.getPwd()));
	    	
	    	log.info("[UserController] registMember request user : " + user);
	    	
	    	userService.registUser(user);
	    	
	    	rttr.addFlashAttribute("message", messageSourceAccessor.getMessage("user.regist"));
	    	
	    	return "redirect:/";
	    }
	    
	    /*아이디 중복 체크*/
	    @PostMapping("/idDupCheck")
	    public ResponseEntity<String> checkDuplication(@RequestBody UserDto user){
	    	
	    	String result = "사용 가능한 아이디입니다.";
	    	log.info("[MemberController] Request Check ID : {}", user.getId());
	    	
	    	if(userService.selectUserById(user.getId())) {
	    		log.info("[UserController] Already Exist");
	    		result = "중복 된 아이디가 존재합니다.";
	    	}    	
	    	
	    	return ResponseEntity.ok(result);
	    }

}
