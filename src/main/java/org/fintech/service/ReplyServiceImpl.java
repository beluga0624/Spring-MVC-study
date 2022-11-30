package org.fintech.service;

import java.util.List;

import org.fintech.domain.Criteria;
import org.fintech.domain.ReplyPageDTO;
import org.fintech.domain.ReplyVO;
import org.fintech.mapper.BoardMapper;
import org.fintech.mapper.ReplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class ReplyServiceImpl implements ReplyService{
	
	@Setter(onMethod_ = {@Autowired})
	private ReplyMapper mapper;
	
	@Setter(onMethod_ = @Autowired)
	private BoardMapper boardMapper;
	
	//댓글 입력과 댓글 수 증가처리를 할때 두 Task가 모두 성공 시 Commit 아니면 Rollback 처리
	@Transactional
	@Override
	public int register(ReplyVO vo) {
		log.info("register............" + vo);
		//댓글 입력 처리 전 댓글수 1 증가시킴 03.21
		boardMapper.updateReplyCnt(vo.getBno(), 1);
		//댓글 처리
		return mapper.insert(vo);
	}

	@Override
	public ReplyVO get(Long rno) {
		log.info("get............" + rno);
		return mapper.read(rno);
	}

	@Override
	public int modify(ReplyVO vo) {
		log.info("modify............" + vo);
		return mapper.update(vo);
	}
	
	@Transactional
	@Override
	public int remove(Long rno) {
		log.info("remove............" + rno);
		ReplyVO vo = mapper.read(rno);
		boardMapper.updateReplyCnt(vo.getBno(), -1);
		return mapper.delete(rno);
	}

	@Override
	public List<ReplyVO> getList(Criteria cri, Long bno) {
		log.info("get Reply List of a board " + bno);
		return mapper.getListWithPaging(cri, bno);
	}

	@Override
	public ReplyPageDTO getListPage(Criteria cri, Long bno) {
		return new ReplyPageDTO(mapper.getCountByBno(bno),
								mapper.getListWithPaging(cri, bno));
	}

}
