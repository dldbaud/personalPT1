package com.greedy.sarada.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.greedy.sarada.user.service.AuthenticationService;
import com.greedy.sarada.user.service.PrincipalOauth2UserService;


//@Configuration
// secured 어노테이션 활성화 매핑시 @Secured("ROLE_ADMIN") 이라고 하면 이 이 권한 접속 가능
// prePostEnabled = true 어노테이션 활성화 @PrePostEnabled("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") 여러개 걸기
//@EnableGlobalMethodSecurity(securedEnabled = true) 
@EnableWebSecurity
public class SecurityConfig {
	
	private final PrincipalOauth2UserService principalOauth2UserService;
	private final AuthenticationService authenticationService;
	
	public SecurityConfig(AuthenticationService authenticationService
			, PrincipalOauth2UserService principalOauth2UserService
			) {
		this.authenticationService = authenticationService;
		this.principalOauth2UserService = principalOauth2UserService;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
			return http
        		.csrf().disable()
                .authorizeRequests()
                .antMatchers("/sell/**", "/admin/**").hasRole("ADMIN")
                .antMatchers("/sell/**").hasRole("SELL")
//                .antMatchers("/user/findId").hasRole("USER")
                // 관리자만 사용 가능한 기능은 현재는 없음
                .anyRequest().permitAll()
                .and()
                    .formLogin()
                    .loginPage("/user/login")             
                    .defaultSuccessUrl("/")  
                    .failureForwardUrl("/user/loginfail")
                    .usernameParameter("id")			// 아이디 파라미터명 설정
                    .passwordParameter("pwd")			// 패스워드 파라미터명 설정
                .and()
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                    .deleteCookies("JSESSIONID")
                    .invalidateHttpSession(true)
                    .logoutSuccessUrl("/")
                .and() 
                	.oauth2Login()
                	.loginPage("/user/login")
                	.userInfoEndpoint()
                	.userService(principalOauth2UserService)
			 		.and()
                .and()
                .build();
//            return http.build();
    }
	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		
		return http
				.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(authenticationService)
				.passwordEncoder(passwordEncoder())
				.and()
				.build();
	}

}















