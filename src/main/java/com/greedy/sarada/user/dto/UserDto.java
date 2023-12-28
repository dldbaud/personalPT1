package com.greedy.sarada.user.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UserDto implements UserDetails, OAuth2User{
	
	private String userNo;									
	private String id;						
	private String pwd;								
	private String userNm;							
	private String profileNm;
	private String phone;
	private String address;
	private Date joinDate;							
	private Date quitDate;							
	private String userStatus;
	private String snsToken;
	private String email;
	private String userType;
	private List<UserRoleDto> userRoleList;
	private Map<String, Object> attributes;
	
	//oauth로그인
	
	public UserDto() {
		// TODO Auto-generated constructor stub
	}

	public UserDto(UserDto user, Map<String, Object> attributes) {
	    this.id = user.getId();
	    this.pwd = user.getPwd();
	    this.phone = user.getPhone();
	    this.userNm = user.getUserNm();
	    this.userRoleList = new ArrayList<>();
		this.attributes = attributes;
	}
	
	


	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        for(UserRoleDto role : userRoleList) {
        	roles.add(new SimpleGrantedAuthority(role.getAuth().getAuthNm()));
        }
        return roles;
	}
	@Override
	public String getPassword() {
		return pwd;
	}
	@Override
	public String getUsername() {
		return id;
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	//휴면 계정
	@Override
	public boolean isEnabled() {
		return true;
	}
	@Override
	public Map<String, Object> getAttributes() {
		
		return attributes;
	}
	//연결 id리턴 (sub리턴)
	@Override
	public String getName() {
		
		return null;
	}
}