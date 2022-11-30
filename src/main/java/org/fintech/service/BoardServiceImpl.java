package org.fintech.service;

import java.util.List;

import org.fintech.domain.BoardAttachVO;
import org.fintech.domain.BoardVO;
import org.fintech.domain.Criteria;
import org.fintech.mapper.BoardAttachMapper;
import org.fintech.mapper.BoardMapper;
import org.fintech.mapper.ReplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
//해당 클래스가 서비스 기능을 수행한다는 어노테이션
@Service
//모든 매개변수를 이용하는 생성자를 생성
@AllArgsConstructor
public class BoardServiceImpl implements BoardService{
	
	@Setter(onMethod_ = @Autowired)
	private BoardMapper mapper;
	
	//첨부파일을 관리하기 위한 인터페이스 자동 주입
	@Setter(onMethod_ = @Autowired)
	private BoardAttachMapper attachMapper;
	
	@Setter(onMethod_ = @Autowired)
	private ReplyMapper replyMapper;
	
	//게시물 등록 버튼을 클릭하면 게시물 테이블과 첨부파일 테이블에 동시에 insert처리를 하므로
	//둘 다 성공하면 Commit 아니면 Rollback처리를 한다
	@Transactional
	@Override
	public void register(BoardVO board) {
		log.info("register.............." + board);
		
		//게시물 테이블에 추가
		mapper.insertSelectKey(board);
		
		//첨부파일이 없으면 종료
		if(board.getAttachList() == null || board.getAttachList().size() <= 0) {
			return;
		}
		
		board.getAttachList().forEach(attach ->{
			//board.getBno(): 해당 게시물 번호 리턴
			attach.setBno(board.getBno());
			attachMapper.insert(attach);
		});
		
	}

	@Override
	public BoardVO get(Long bno) {
		
		log.info("get.............." + bno);
		
		return mapper.read(bno);
	}
	
	@Transactional
	@Override
	public boolean modify(BoardVO board) {
		log.info("modify............" + board);
		
		attachMapper.deleteAll(board.getBno());
		
		boolean modifyResult = mapper.update(board) == 1;
		
		if(modifyResult && board.getAttachList() != null && board.getAttachList().size() > 0) {
			board.getAttachList().forEach(attach -> {
				attach.setBno(board.getBno());
				attachMapper.insert(attach);
			});
		}
		
		return modifyResult;
	}
	
	@Transactional
	@Override
	public boolean remove(Long bno) {
		log.info("remove..........." + bno);
		
		attachMapper.deleteAll(bno);
		
		replyMapper.deleteAll(bno);
		
		return mapper.delete(bno) == 1;
	}
	
	//페이징 처리
	@Override
	public List<BoardVO> getList(Criteria cri) {
		log.info("get List with criteria: " + cri);
		
		return mapper.getListWithPaging(cri);
	}
	
	@Override
	public int getTotal(Criteria cri) {
		log.info("get total count");
		return mapper.getTotalCount(cri);
	}

	@Override
	public List<BoardAttachVO> getAttachList(Long bno) {
		log.info("get Attach list by bno" + bno);
		return attachMapper.findByBno(bno);
	}

}
