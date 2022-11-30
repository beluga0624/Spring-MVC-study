package org.fintech.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PageDTO {
	private int startPage; //화면 하단의 시작페이지
	private int endPage; //종려 페이지
	private boolean prev, next; //prev 이전 페이지 체크, next 다음 페이지 치크 
	private int total; //전체 게시물 수
	private Criteria cri;
	
	public PageDTO(Criteria cri, int total) {
		this.cri = cri;
		this.total = total;
		this.endPage = (int)(Math.ceil(cri.getPageNum() / 10.0)) * 10;
		this.startPage = this.endPage - 9;
		//실제 계산에 의한 페이지 수
		int realEnd = (int)(Math.ceil((total * 1.0) / cri.getAmount()));
		// 히단 마지막 페이지 처리시 계산에 의한 페이지수보다 크면 계산에 의한 페이지 수를 마지막 페이지로 처리 
		if(realEnd < this.endPage) {
			this.endPage = realEnd;
		}
		
		//2페이지가 존재하면 1페이지로 이동이 가능하므로 true 2페이지가 없으면 false
		this.prev = this.startPage > 1;
		//하단 endPage가 계산에 의한 페이지 수보다 작으면 다음페이지가 존재하므로 true 아니면 false
		this.next = this.endPage < realEnd;
	}
}
