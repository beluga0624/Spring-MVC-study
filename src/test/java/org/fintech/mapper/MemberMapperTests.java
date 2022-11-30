package org.fintech.mapper;

import org.fintech.domain.MemberVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
@Log4j
public class MemberMapperTests {
	@Setter(onMethod_ = @Autowired)
	private MemberMapper mapper;
	
	@Test
	public void testRead() {
		//admin90에 대한 사용자 정보와 권한 내역을 가져와 변수에 대입
		MemberVO vo = mapper.read("admin90");
		
		log.info(vo);
		
		//vo.getAuthList() : mapping된 특정사용자에 대한 권한 리스트를 리턴
		vo.getAuthList().forEach(authVO -> log.info(authVO));
		
	}
}
