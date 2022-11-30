package org.fintech.service;

import java.util.List;

import org.fintech.domain.BoardAttachVO;
import org.fintech.domain.BoardVO;
import org.fintech.domain.Criteria;

public interface BoardService {
	//게시물 등록
	public void register(BoardVO board);
	
	//특정 게시물 가져오기
	public BoardVO get(Long bno);
	
	//게시물 업데이트
	public boolean modify(BoardVO board);
	
	//특정 게시물 삭제
	public boolean remove(Long bno);
	
	//모든 게시물 리스트 가져오기
	public List<BoardVO> getList(Criteria cri);
	
	public int getTotal(Criteria cri);
	
	public List<BoardAttachVO> getAttachList(Long bno);
}
