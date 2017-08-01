package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	// 임시적으로 보안요소를 해지했다. 로그인 이후에 사용할 수 있는 서비스는 접근을 제어한다. 
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(				
				"/webjars/**",
				"/css/**", "/fonts/**", "/img/**", "/js/**", "/vendor/**",	// bootstrap part 
				"/twitter/**", "/users/**", "/index/**", "/tourism/**", "/recommendation/**", "/developer/**",	// site part 
				"/console/**");	// Inner DB 사용 허가
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/loginForm").permitAll() // 인증을 거칠 필요 없는 페이지 목록 , 일단은 전부다 접속 가능하게 했다. 
			.anyRequest().authenticated();	
		http.formLogin()
			.loginProcessingUrl("/login")
			.loginPage("/loginForm")
			.failureUrl("/loginForm?error")
			.defaultSuccessUrl("/index", true)    
			.usernameParameter("siteId").passwordParameter("password")
			.and();
		http.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout**"))
			.logoutSuccessUrl("/loginForm");
	}
	
	@Configuration
	static class AuthenticationConfiguration 
			extends GlobalAuthenticationConfigurerAdapter {
		@Autowired
		UserDetailsService userDetailsService;
		
		@Bean
		PasswordEncoder passwordEncoder(){
			return new PasswordEncoder() {				
				@Override
				public boolean matches(CharSequence arg0, String arg1) {														
					// arg0, arg1 일치하는 경우 ID, PW가 유효하다. 
					System.out.println("arg0 = " + arg0 + ", arg1 = " + arg1);
					if(arg0.equals(arg1))
						return true;
					else 
						return false;
				}				
				@Override
				public String encode(CharSequence arg0) {
					// TODO Auto-generated method stub
					return null;
				}
			};
		}
		
		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder());
		}
	}
}