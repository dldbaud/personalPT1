package com.greedy.sarada.user.service;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.greedy.sarada.user.dao.UserMapper;
import com.greedy.sarada.user.dto.UserDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthenticationService implements UserDetailsService {

    private final UserMapper mapper;
    private static String loginStatus;
    public AuthenticationService(UserMapper mapper) {
        this.mapper = mapper;
    }
    
    public static String getLoginStatus() {
        return loginStatus;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

   
        log.info("[AuthenticationService] memberId : {}", id);
        
        UserDto user = mapper.findByUserId(id);

        log.info("[AuthenticationService] member : {}", user);
        
        
        if(user == null){


            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다");            	
            
        	
        }
        
        log.info("[AuthenticationService] getUsername : {}", user.getUsername());
        //이거 없이 처음부터 위에서 status가 y인 조건을 줘서 검색하면 편함

        
        return user;
    }

}
