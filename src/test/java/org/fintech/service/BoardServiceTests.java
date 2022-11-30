package org.fintech.service;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import org.fintech.domain.BoardVO;
import org.fintech.domain.Criteria;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class BoardServiceTests {
	@Setter(onMethod_ = {@Autowired})
	private BoardService service;
	
	@Ignore
	public void testExist() {
		log.info(service);
		//해당 클래스가 빈 등록이 되어있으면 true
		assertNotNull(service);
	}
	
	@Ignore
	public void testRegister() {
		BoardVO board = new BoardVO();
		board.setTitle("새로 작성하는 글");
		board.setContent("새로 작성하는 내용");
		board.setWriter("newbie");
		
		service.register(board);
		
		log.info("생성된 게시물의 번호: " + board.getBno());
	}
	
	@Ignore
	public void testGetList() {
		service.getList(new Criteria(1,10)).forEach(board -> log.info(board));
	}
	
	@Ignore
	public void testGet() {
		log.info(service.get(2L));
	}
	
	@Ignore
	public void testDelete() {
		log.info("REMOVE RESULT: " + service.remove(3L));
	}
	
	@Test
	public void testUpdate() {
		BoardVO board = service.get(2L);
		
		if(board == null) {
			return;
		}
		
		board.setTitle("제목 수정합니다.");
		log.info("MODIFY RESULT: " + service.modify(board));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
