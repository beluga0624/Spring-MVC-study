package org.fintech.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import lombok.extern.log4j.Log4j;

//AuthenticationSuccessHandler : 로그인 성공시 처리를 담당하는 인터페이스
//HttpServletRequest : 웹에서 Request 값을 가지는 객체
//HttpServletResponse : 웹에서 Response 값을 가지는 객체
//Authentication : 인증에 성공한 사용자의 정보를 가지는 객체
@Log4j
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler{@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		log.warn("Login Success");
		
		List<String> roleNames = new ArrayList<>();
		
		//사용자에 대한 권한 목록을 리턴받아 반복문을 통해 ArrayList에 대입
		authentication.getAuthorities().forEach(authority -> {
			roleNames.add(authority.getAuthority());
		});
		
		log.warn("ROLE NAMES: " + roleNames);
		
		//배열의 권한 중 ROLE_ADMIN을 포함하고 있으면 처리
		if(roleNames.contains("ROLE_ADMIN")) {
			response.sendRedirect("/sample/admin");
			return;
		}
		
		//배열의 권한 중 ROLE_MEMBER를 포함하고 있으면 처리
		if(roleNames.contains("ROLE_MEMBER")) {
			response.sendRedirect("/sample/member");
			return;
		}
		
		response.sendRedirect("/");
		
	}

}
