package org.fintech.domain;

import lombok.Data;

@Data
public class AttachFileDTO {
	private String fileName; //업로드 파일명
	private String uploadPath; //업로드 경로
	private String uuid; //중복을 막기 위한 UUID값
	private boolean image; //업로드 파일이 이미지인지 여부
}
