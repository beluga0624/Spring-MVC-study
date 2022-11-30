package org.fintech.mapper;

import org.fintech.domain.MemberVO;

public interface MemberMapper {
	//특정 사용자 아이디에 대한 사용자 내역을 가져오는 메서드
	public MemberVO read(String userid);
}
