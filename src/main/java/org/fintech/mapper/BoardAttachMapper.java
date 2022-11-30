package org.fintech.mapper;

import java.util.List;

import org.fintech.domain.BoardAttachVO;

//첨부파일을 관리하기 위한 인터페이스
//첨부파일은 수쟁개념이 기존자료 삭제후 insert 처리
public interface BoardAttachMapper {
	//특정 게시물에 대한 첨부파일 추가 처리
	public void insert(BoardAttachVO vo);
	//특정 게시물에 대한 첨부파일 삭제 처리
	public void delete(String uuid);
	//특정 게시물에 대한 첨부파일 리스트 가져오기
	public List<BoardAttachVO> findByBno(Long bno);
	//특정 게시물삭제시 그 게시물의 모든 첨부파일 삭제 처리
	public void deleteAll(Long bno);
	//서버 첨부파일 디렉토리와 테이블을 비교하여 불필요한 파일 삭제를 위해 어제기준 파일리스트를 가져오는 메서드 선언
	public List<BoardAttachVO> getOldFiles();
}
