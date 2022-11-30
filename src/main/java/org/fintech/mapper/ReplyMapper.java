package org.fintech.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.fintech.domain.Criteria;
import org.fintech.domain.ReplyVO;

public interface ReplyMapper {
	//댓글 등록
	public int insert(ReplyVO vo);
	
	//특정 댓글 읽기
	public ReplyVO read(Long rno);
	
	//특정 댓글 삭제
	public int delete(Long rno);
	
	//댓글 수정
	public int update(ReplyVO reply);
	
	public List<ReplyVO> getListWithPaging(@Param("cri") Criteria cri,
										   @Param("bno") Long bno);
	
	public int getCountByBno(Long bno);
	
	public int deleteAll(Long bno);
}
