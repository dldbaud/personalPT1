package com.greedy.sarada.user.service;

import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.greedy.sarada.common.exception.user.MemberRegistException;
import com.greedy.sarada.provider.GoogleUserInfo;
import com.greedy.sarada.provider.NaverUserInfo;
import com.greedy.sarada.provider.OAuth2UserInfo;
import com.greedy.sarada.provider.SnsDto;
import com.greedy.sarada.user.dao.UserMapper;
import com.greedy.sarada.user.dto.UserDto;

import lombok.extern.slf4j.Slf4j;

//sns요청 완료 후 후처리
@Slf4j
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{
	
	private final PasswordEncoder passwordEncoder;
	private final UserMapper mapper;
	private final UserService userService;
	 
	public PrincipalOauth2UserService(UserMapper mapper, UserService userService) {
		this.passwordEncoder = new BCryptPasswordEncoder();
		this.mapper = mapper;
		this.userService = userService;
	}
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
    	log.info("[PrincipalOauth2UserService] OAuth2User getClientRegistration : " + userRequest.getClientRegistration());//sns 확인
    	log.info("[PrincipalOauth2UserService] OAuth2User getAccessToken : " + userRequest.getAccessToken().getTokenValue());
    	log.info("[PrincipalOauth2UserService] OAuth2User getClientRegistration : " + userRequest.getClientRegistration());
    	OAuth2User oauth2User = super.loadUser(userRequest);
//    	log.info("[PrincipalOauth2UserService] OAuth2User getAttribute : " + super.loadUser(userRequest).getAttributes());    	
    	log.info("[PrincipalOauth2UserService] OAuth2User getAttribute : " + oauth2User.getAttributes());
    	String userType = "";
    	OAuth2UserInfo oAuth2UserInfo = null;
    	if ("google".equals(userRequest.getClientRegistration().getRegistrationId())) {
    		log.info("[구글 요청]");
    		userType = "google";
    		oAuth2UserInfo = new GoogleUserInfo(oauth2User.getAttributes());
    	} else if("naver".equals(userRequest.getClientRegistration().getRegistrationId())) {
    		log.info("[네이버 요청]");
    		//reponse 안에 response 안에 있기 때문
    		userType = "naver";
    		oAuth2UserInfo = new NaverUserInfo((Map)oauth2User.getAttributes().get("response"));
    	} else {
    		log.info("[구글과 네이버만 가능]");
    	}
    
    	String provider = oAuth2UserInfo.getProvider();
    	String providerId = oAuth2UserInfo.getProviderId();
    	String email = oAuth2UserInfo.getEmail();
    	String userName = provider + "_" + providerId;
    	String name = oAuth2UserInfo.getName();
    	String password = passwordEncoder.encode("아무문자");
    	String mobile = oAuth2UserInfo.getMobile();
    	
        UserDto user = new UserDto();
        SnsDto sns = new SnsDto();
        
    	if((userService.selectUserProviderId(userName))) {
    		log.info("[PrincipalOauth2UserService] 가입가능");
    		log.info("[PrincipalOauth2UserService] id확인" + providerId);
    		user.setId(email);
    		user.setPwd(password);
    		user.setUserNm(name);
    		user.setPhone(mobile);
    		user.setUserType(userType);
    		sns.setSnsId(userName);
    		sns.setSnsNm(provider);
    		try {
    			log.info("[PrincipalOauth2UserService] user" + user);
    			userService.registSnsUser(user, sns);
    		} catch (MemberRegistException e) {
    			e.printStackTrace();
    		}
    	} else {
    		log.info("[PrincipalOauth2UserService] 이미가입");
    		user = mapper.findByUserId(email);
    		log.info("[PrincipalOauth2UserService] user" + user);
    	}
    	
    	return new UserDto(user, oauth2User.getAttributes());
	}
}
