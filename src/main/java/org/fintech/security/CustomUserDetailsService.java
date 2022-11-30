package org.fintech.security;

import org.fintech.domain.MemberVO;
import org.fintech.mapper.MemberMapper;
import org.fintech.security.domain.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

//UserDetailsService : DaoAuthenticationProvider와 협력하는 인터페이스로 요청받은 사용자의 아이디, 비밀번호를 검증하는 역할을 수행
@Log4j
public class CustomUserDetailsService implements UserDetailsService{
	@Setter(onMethod_ = @Autowired)
	private MemberMapper memberMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.warn("Load User By UserName: " + username);
		
		MemberVO vo = memberMapper.read(username);
		
		log.warn("queried by member mapper: " + vo);
		
		return vo == null? null : new CustomUser(vo);
	}
	
	
}
