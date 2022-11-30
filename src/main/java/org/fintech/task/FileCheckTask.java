package org.fintech.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.fintech.domain.BoardAttachVO;
import org.fintech.mapper.BoardAttachMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class FileCheckTask {
	@Setter(onMethod_ = @Autowired)
	private BoardAttachMapper attachMapper;
	
	//어제 기준 첨부파일 폴더 리턴 
	private String getFolderYesterDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//싱글톤 패턴(메모리 효율)
		Calendar cal = Calendar.getInstance();
		//전일자 계산
		cal.add(Calendar.DATE, -1);
		
		String str = sdf.format(cal.getTime());
		//"-"을 파일 구분자로 변환(윈도우: \)
		return str.replace("-", File.separator);
	}
	
	//툭종 Task를 주기적으로 처리해주는 어노테이션
	//(cron="초 분 시 일 월 년도")
	@Scheduled(cron="0 0 2 * * *")
	public void checkFiles() throws Exception{
		log.warn("File Check Task run...............");
		log.warn(new Date());
		//현재 기준 전일 첨부파일 리스트를 리턴받아 fileList 참조변수에 대입
		List<BoardAttachVO> fileList = attachMapper.getOldFiles();
		
		//stream : 1.스트림 선언, 2.중간연산(Filter, sort), 3.최종연산(sum, avg, count 등 통계함수)
		//map : 요소들을 조건에 해당하는 값으로 변환처리
		//Paths : File 객체보다 향상된 객체로 해당 파일의 경로를 가져오는 클래스
		//Collectors.toList() : 리턴된 결과를 리스트 형태로 생성 
		List<Path> fileListPaths = fileList.stream().map(vo -> 
			Paths.get("C:\\upload", vo.getUploadPath(), vo.getUuid() + "_" + vo.getFileName())).collect(Collectors.toList());
		
		//필터를 이용하여 전일 첨부파일 중 이미지이면 썸네일 파일도 fileListPaths에 추가
		fileList.stream().filter(vo -> vo.isFileType() == true)
			.map(vo -> Paths.get("C:\\upload", vo.getUploadPath(), "s_" + vo.getUuid() + "_" + vo.getFileName())).forEach(p -> fileListPaths.add(p));
		
		log.warn("====================================================");
		
		fileListPaths.forEach(p -> log.warn(p));
		//삭제하려는 전일 폴더를 targetDir에 대입
		File targetDir = Paths.get("C:\\upload", getFolderYesterDay()).toFile();
		
		//fileListPaths : 첨부파일 테이블(tbl_attach)
		//listFile : 해당 폴더에 있는 파일 리스트를 가져와 각 file이 fileListPaths에는 없다면 removeFiles 에 추가
		File[] removeFiles = targetDir.listFiles(file -> fileListPaths.contains(file.toPath()) == false);
		
		log.warn("----------------------------------------------------");
		for(File file : removeFiles) {
			log.warn(file.getAbsolutePath());
			file.delete();
		}
	}
}
