package org.fintech.mapper;

import java.util.List;

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
public class BoardMapperTests {
	//BaordMapperTests 클래스가 컴파일 시점에 BoardMapper 클래스를 Autowired 한다
	@Setter(onMethod_ = @Autowired)
	private BoardMapper mapper;
	
	@Ignore
	public void testGetList() {
		mapper.getList().forEach(board -> log.info(board));
	}
	
	@Ignore
	public void testInsert() {
		BoardVO board = new BoardVO();
		board.setTitle("새로 작성하는 글");
		board.setContent("새로 작성하는 내용");
		board.setWriter("newbie");
		
		mapper.insert(board);
		
		log.info(board);
	}
	
	@Ignore
	public void testInsertSelectKey() {
		BoardVO board = new BoardVO();
		board.setTitle("새호 작성하는 글 select key");
		board.setContent("새호 작성하는 내용 select key");
		board.setWriter("newbie");
		
		mapper.insertSelectKey(board);
		
		log.info(board);
	}
	
	@Ignore
	public void testRead() {
		BoardVO board = mapper.read(3L);
		
		log.info(board);
	}
	
	@Ignore
	public void testDelete() {
		log.info("Delete Count: " + mapper.delete(4L));
	}
	
	@Ignore
	public void testUpdate() {
		BoardVO board = new BoardVO();
		
		board.setBno(5L);
		board.setTitle("수정된 제목");
		board.setContent("수정된 내용");
		board.setWriter("user00");
		
		int count = mapper.update(board);
		log.info("Update Count: " + count);
	}
	
	@Ignore
	public void testPaging() {
		Criteria cri = new Criteria();
		cri.setPageNum(1);
		cri.setAmount(10);
		List<BoardVO> list = mapper.getListWithPaging(cri);
		list.forEach(board -> log.info(board));
	}
	
	@Test
	public void testSearch() {
		Criteria cri = new Criteria();
		cri.setKeyword("새로");
		cri.setType("TC");
		
		List<BoardVO> list = mapper.getListWithPaging(cri);
		list.forEach(board -> log.info(board));
	}
}
