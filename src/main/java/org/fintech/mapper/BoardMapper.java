package org.fintech.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.fintech.domain.BoardVO;
import org.fintech.domain.Criteria;

public interface BoardMapper {
	//게시물 테이블에서 모든 자료르 가져옴
	public List<BoardVO> getList();
	
	//페이지 처리와 보드 리스트 가져오기
	public List<BoardVO> getListWithPaging(Criteria cri);
	
	//테이블에 신규 게시물 등록
	public void insert(BoardVO board);
	
	//테이불에 신규 게시물 등록을 하되 게시물 번호를 미리 들고와 처리
	public void insertSelectKey(BoardVO board);
	
	//특정 게시물을 가져온다
	public BoardVO read(Long bno);
	
	//특정 게시물 삭제
	public int delete(Long bno);
	
	//게시물 업데이트
	public int update(BoardVO board);
	
	//전체 데이터의 개수 처리
	public int getTotalCount(Criteria cri);
	
	//댓글 수 업데이트 처리
	public void updateReplyCnt(@Param("bno") Long bno, @Param("amount") int amount);
}


