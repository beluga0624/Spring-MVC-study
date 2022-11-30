package org.fintech.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.fintech.domain.AttachFileDTO;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Log4j
public class UploadController {
	
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String str = sdf.format(date);
		return str.replace("-", File.separator);
	}
	
	@GetMapping("/uploadForm")
	public void uploadForm() {
		log.info("upload form");
	}
	
	@PostMapping("/uploadFormAction")
	//MultipartFile[] : 업로드한 파일들이 배열형태로 대입
	//Model : 뷰(jsp)에 값을 전달하기 위한 객체
	public void uploadFormPost(MultipartFile[] uploadFile, Model model) {
		
		String uploadFolder = "C:\\upload";
		
		for(MultipartFile multipartFile : uploadFile) {
			log.info("--------------------------------");
			//업록드하는 원본 파일 이름
			log.info("Upload File Name: " + multipartFile.getOriginalFilename());
			log.info("Upload File size: " + multipartFile.getSize());
			
			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());
			
			try {
				//지정된 upload 폴더에 파일 저장
				multipartFile.transferTo(saveFile);
			}catch(Exception e) {
				log.error(e.getMessage());
			}
		}
	}
	
	@GetMapping("/uploadAjax")
	public void uploadAjax() {
		log.info("upload ajax");
	}
	
	//uploadAjax.jsp의 ajax 실행되어 처리하는 부분
	//ajax에서 전달된 갑을 uploadFile 변수에 대입
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value= "/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile) {

		List<AttachFileDTO> list = new ArrayList<>();
		String uploadFolder = "C:\\upload";
		
		String uploadFolderPath = getFolder();
		
		File uploadPath = new File(uploadFolder, uploadFolderPath);
		log.info("upload path: " + uploadPath);
		
		//업로드 폴더가 존재하는지 체크해서 없으면 하위 폴더까지 생성
		if(uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}
		
		for(MultipartFile multipartFile : uploadFile) {
			log.info("------------------------------------");
			log.info("Upload File Nmae: " + multipartFile.getOriginalFilename());
			log.info("Upload File Size: " + multipartFile.getSize());
			
			AttachFileDTO attachDTO = new AttachFileDTO();
			
			String uploadFileName = multipartFile.getOriginalFilename();
			
			//IE has file path
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\")+1);
			log.info("only file name: " + uploadFileName);
			attachDTO.setFileName(uploadFileName);
			
			//파일 업로드시 중복을 배제하기 위해서 uuid값을 랜덤으로 발생시켜 업로드 파일명에 적용
			UUID uuid = UUID.randomUUID();
			uploadFileName = uuid.toString() + "_" + uploadFileName; 
			
			
			
			try {
				File saveFile = new File(uploadPath, uploadFileName);
				multipartFile.transferTo(saveFile);
				
				attachDTO.setUuid(uuid.toString());
				attachDTO.setUploadPath(uploadFolderPath);
				
				if(checkImageType(saveFile)) {
					
					attachDTO.setImage(true);
					
					FileOutputStream thumbnail =
						//바이트단위 출력 보조 스트림
						//s_ 를 파일명앞에 붙여 thumbnail 생성
						new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
					//100 * 100 크기의 썸네일 생성
					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);
					
					thumbnail.close();
				}
				
				list.add(attachDTO);
			}catch(Exception e){
				log.error(e.getMessage());
			}
		}
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	//파일 업로드시 이미지에 대해서만 썸네일을 생성하기 위해 업로드하는 파일이 이미지 인지 체크하는 메서드
	private boolean checkImageType(File file) {
		try {
			// image/jpeg, image/gif, video/mp4, video/avi 등(MIME)
			//probeContentType : 클라이언트에서 업로드한 파일의 MIME값을 가져오는 메서드
			String contentType = Files.probeContentType(file.toPath());
			//MIME 문자열이 image로 시작하면 업로드 파일의 종류는 이미지임을 알수 있다.
			return contentType.startsWith("image");
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	@GetMapping("/display")
	//서버가 클라이언트에 응답처리시 자바클래스를 HTTP 응답 본문의 객체로 변환하여 전송처리하는 어노테이션 
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName){
		log.info("fileName: " + fileName);
		File file = new File("c:\\upload\\" + fileName);
		
		log.info("file: " + file);
		
		ResponseEntity<byte[]> result = null;
		
		try {
			//헤더의 정보를 가지고 있는 객체 선언
			HttpHeaders header = new HttpHeaders();
			//매개변수로 받은 파일의 MIME 값을 헤더정보에 추가
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			//파일을 스트림 복사하고 사용 후 스트림을 무조건 close 처리
			//보려는 이미지를 inputStream을 이용하여 byte배열에 대입처리하는 메서드
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	@GetMapping(value = "/download", 
				//클아이언트에게 응답처리시 다운로드 처리를 할 수 있도록 선언
				produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	//Resource : 자원에 대한 정보를 가지고 있는 객체		
	//RequestHeader : Http요청 헤더 정보를 해당 메서드의 매개변수로 전달하는 어노테이션
	//User-Agent : 현재 사용중인 브라우저 정보
	public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent, String fileName){
		log.info("download file: " +fileName);
		
		//FileSystemResource : 특정파일의 세부 정보를 리턴하는 객체
		Resource resource = new FileSystemResource("c:\\upload\\" + fileName);
		
		if(resource.exists() == false) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		String resourceName = resource.getFilename();
		
		//remove UUID
		String resourceOriginalName = resourceName.substring(resourceName.indexOf("_") + 1);
		
		HttpHeaders headers = new HttpHeaders();
		
		try {
			String downloadName = null;
			
			//사용중인 브라우저별로 파일 다운로드 처리
			if(userAgent.contains("Trident")) {
				log.info("IE browser");
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8").replaceAll("\\+", " ");
			}else if(userAgent.contains("Edge")) {
				log.info("Edge browser");
				downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8");
				log.info("Edge name: " + downloadName);
			}else {
				log.info("Chrome browser");
				downloadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1");
			}
			
			headers.add("Content-Disposition", "attachment; filename=" + downloadName);
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
	}
	
	//삭제처리
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/deleteFile")
	//자바 객체를 Http요청의 Body내용으로 Mapping 하는 역할을 하는 어노테이션
	@ResponseBody
	//ResponseEntity : HttpEntity를 상속받는 것으로 HttpHeader와 Body로 구성된 객체로 
	//					데이터와 http 처리 결과 코드값을 함께 전송하는 객체
	public ResponseEntity<String> deleteFile(String fileName, String type){
		log.info("deleteFile: " + fileName);
		File file;
		
		try {
			file = new File("C:\\upload\\" + URLDecoder.decode(fileName, "UTF-8"));
			
			file.delete();
			
			if(type.equals("image")) {
				//썸네일 파일의 원본 이미지 파일명
				String largeFileName = file.getAbsolutePath().replace("s_", "");
				log.info("largeFileName: " + largeFileName);
				file = new File(largeFileName);
				file.delete();//원본파일 삭제처리
			}
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);//404
		}
		
		//ResponseEntity 리턴 하기전 한글 처리
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "text/html; charset=utf-8");
		
		return new ResponseEntity<String>("정상적으로 삭제처리", header, HttpStatus.OK);//200
	}
	
}
