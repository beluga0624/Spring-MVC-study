package org.fintech.security.domain;

import java.util.Collection;
import java.util.stream.Collectors;

import org.fintech.domain.MemberVO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;

@Getter
public class CustomUser extends User{
	//버전관리
	private static final long serialVersionUID = 1L;
	
	//사용자 정보 & 권한리스트를 가지는 클래스
	private MemberVO member;
	
	public CustomUser(String username,//유저 id 
					  String password, //유저 pw
					  Collection<? extends GrantedAuthority> authorities ) {//유저 권한리스트
		//부모클래스인 User 클래스의 매개변수 3개짜리 생성자 호출
		super(username, password, authorities);
	}
	
	//Overloading : 메서드나 생성자의 이름은 같으나 매개변수의 갯수, 순서, 타입 등을 다르게해서 
	//여러개를 생성하는 기법
	//Overriding : 부모클래스를 상속받아 부모클래스의 메서드를 재정의하는 기법 
	public CustomUser(MemberVO vo) {
		super(vo.getUserid(), vo.getUserpw(), vo.getAuthList().stream() 
				.map(auth -> new SimpleGrantedAuthority(auth.getAuth())).
				collect(Collectors.toList()));
		//스트림 선언 + 중간연산(filter, sort) + 최종연산(sum, avg, count, collect)
		
		this.member = vo;
	}

}
