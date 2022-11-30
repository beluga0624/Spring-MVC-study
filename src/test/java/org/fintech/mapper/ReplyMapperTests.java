package org.fintech.mapper;

import java.util.List;
import java.util.stream.IntStream;

import org.fintech.domain.Criteria;
import org.fintech.domain.ReplyVO;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class ReplyMapperTests {
	private Long[] bnoArr = {24586L, 24583L, 24581L, 24577L};
	
	@Setter(onMethod_ = {@Autowired})
	private ReplyMapper mapper;
	
	@Test
	public void testMapper() {
		log.info(mapper);
	}
	
	@Ignore
	public void testCreate() {
		//1부터 10까지 반복
		IntStream.rangeClosed(1, 10).forEach(i -> {
			ReplyVO vo = new ReplyVO();
			//배열에 해당하는 값을 가져와 게시물 번호로 지정
			vo.setBno(bnoArr[i % 4]);
			vo.setReply("댓글 테스트" + i);
			vo.setReplyer("replyer" + i);
			
			mapper.insert(vo);
		});
	}
	
	@Ignore
	public void testRead() {
		Long targetRno = 5L;
		
		ReplyVO vo = mapper.read(targetRno);
		
		log.info(vo);
	}
	
	@Ignore
	public void testDelete() {
		Long targetRno = 4L;
		mapper.delete(targetRno);
	}
	
	@Ignore
	public void testUpdate() {
		Long targetRno = 10L;
		//특정 댓글을 수정하기위해 댓글의 내역을 불러와 저장 후 수정
		ReplyVO vo = mapper.read(targetRno);
		vo.setReply("Update Reply");
		int count = mapper.update(vo);
		log.info("UPDATE COUNT: " + count);
	}
	
	@Ignore
	public void testList() {
		Criteria cri = new Criteria();
		List<ReplyVO> replies = mapper.getListWithPaging(cri, bnoArr[0]);
		replies.forEach(reply -> log.info(reply));
	}
	
	@Test
	public void testList2() {
		Criteria cri = new Criteria(2, 10);
		List<ReplyVO> replies = mapper.getListWithPaging(cri, 24583L);
		replies.forEach(reply -> log.info(reply));
	}
}
