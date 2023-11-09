package com.greedy.sarada.user.controller;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.greedy.sarada.common.exception.user.MemberModifyException;
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

	        return "user/login/login";
	    }
	    
	    /* 회원가입 페이지 이동 */
	    @GetMapping("/regist")
	    public String goAgree() {
	    	return "user/login/registAgree";
	    }
	    
	    @GetMapping("/agreeRegist")
	    public String goRegist() {
	    	return "user/login/regist";
	    }
	    /* 로그인 실패 시 */
	    @PostMapping("/loginfail")
	    public String loginFailed(RedirectAttributes rttr) {
	    	
	    	rttr.addFlashAttribute("message", messageSourceAccessor.getMessage("error.login"));
	    	
	    	return "redirect:/user/login/login";
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
	    
	    /*마이페이지*/
	    @GetMapping("/myProfile")
	    public String myProFile() {
	    	
	    	return "user/myPage/myProfile";
	    }
	    
	    /*회원 정보 수정 화면이동*/
	    @GetMapping("/myProfile/update")
	    public String myProfileUpdate(@AuthenticationPrincipal UserDto loginUser, Model model) {
	    	
	    	String[] address = loginUser.getAddress().split("\\$");
	    	
	    	model.addAttribute("address", address);
	    	
	    	return "user/myPage/profileUpdate";
	    }
	    
	    /*회원 정보 수정*/
	    @PostMapping("/update")
	    public String modifyUser(@ModelAttribute UserDto updateUser,
	    		@RequestParam String zipCode, @RequestParam String address1, @RequestParam String address2,
	    		@AuthenticationPrincipal UserDto loginUser,
	    		RedirectAttributes rttr) throws MemberModifyException {
	    
	    	String address = zipCode + "$" + address1 + "$" + address2;
	    	updateUser.setAddress(address);
	    	
	    	updateUser.setUserNo(loginUser.getId());
	    	
	    	log.info("[UserController] modifyUser : {}",updateUser);
	    	
	    	userService.modifyUser(updateUser);
	    	
	    	SecurityContextHolder.getContext().setAuthentication(createNewAuthentication(loginUser.getId()));
	    	
	    	rttr.addFlashAttribute("message", messageSourceAccessor.getMessage("user.modify"));
	    	return "redirect:/";
	    }
	    
	    /* 회원 정보 수정 시 세션에 저장 된 정보 업데이트 */
	    private Authentication createNewAuthentication(String id) {
	    	
	    	UserDetails newPrincipal = authenticationService.loadUserByUsername(id);
	    	UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(newPrincipal, newPrincipal.getPassword(), newPrincipal.getAuthorities());

	        return newAuth;
		}

		/* 사업자 등록 페이지*/
	    @GetMapping("/sell/sellRegist")
	    public String sellRegist() {
	    	
	    	return "user/sell/sellRegist";
	    }
	    
}
