package org.fintech.domain;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Criteria {
	private int pageNum;//페이지 번호
	private int amount;// 한페이지당 보여줄 행수
	private String type;
	private String keyword;
	
	public Criteria() {
		this(1,10);//매개변수 2개짜리 생성자 호출(1페이지 이고 10행 조회)
	}
	
	public Criteria(int pageNum, int amount) {
		this.pageNum = pageNum;
		this.amount = amount;
	}
	
	//검색을 위한 처리
	//검색 조건을 선택하지 않으면 문자열 배열을 초기화 하고
	//검색 조건을 선택하면 검색조건을 분리하여 문자열 배열에 대입
	public String[] getTypeArr() {
		return type == null? new String[] {}: type.split("");
	}
	
	public String getListLink() {
		//UriComponentsBuilder : 전송방식 get을 사용하는 경우 여러 개의 파라미터들을 연결하여 URL 형태로 만들어 주는 기능
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
				.queryParam("pageNum", this.pageNum)
				.queryParam("amount", this.getAmount())
				.queryParam("type", this.getType())
				.queryParam("keyword", this.getKeyword());
		
		return builder.toUriString();
	}
}
