package org.fintech.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

//tbl_board 테이블과 매핑처리를 하기위한 클래스
@Data
public class BoardVO {
	private Long bno;
	private String title;
	private String content;
	private String writer;
	private Date regdate;
	private Date updateDate;
	//댓글 수 추가 03.21
	private int replyCnt;
	
	//첨부파일 리스트를 관리하기위한 필드 선언
	private List<BoardAttachVO> attachList;
}
