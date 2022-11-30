package org.fintech.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.fintech.domain.BoardAttachVO;
import org.fintech.domain.BoardVO;
import org.fintech.domain.Criteria;
import org.fintech.domain.PageDTO;
import org.fintech.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/board/*")
//모든 필드 값을 매개변수로 받는 생성자를 생성하는 어노테이션
@AllArgsConstructor
public class BoardController {
	
	private BoardService service;
	
	//03.14 페이징 처리 (Criteria) 추가
	@GetMapping("/list")
	public void list(Criteria cri, Model model) {
		log.info("list: " + cri);
		model.addAttribute("list", service.getList(cri));
		
		int total = service.getTotal(cri);
		log.info("total: " + total);
		model.addAttribute("pageMaker", new PageDTO(cri, total));
	}
	
	@GetMapping("/register")
	@PreAuthorize("isAuthenticated()")
	public void register() {
		
	}
	
	@PostMapping("/register")
	@PreAuthorize("isAuthenticated()")
	public String register(BoardVO board, RedirectAttributes rttr) {
		log.info("=============================");
		log.info("register: " + board);
		
		//특정 게시물 번호에 대한 첨부파일 내역이 존재하면 처리
		if(board.getAttachList() != null) {
			board.getAttachList().forEach(attach -> log.info(attach));
		}
		
		log.info("=============================");
		
		service.register(board);
		//신규 게시물 처리를 한 후에 방금 추가한 게시물 번호를 일회성 속성으로 지정하여 View에 전달
		rttr.addFlashAttribute("result", board.getBno());
		
		//모든 flashAttributes를 redirect하기전에 session에 복사해두고 redirect 한 후에 
		//session에 있는 flashAttributes를 Model로 이동처리 한다.
		return "redirect:/board/list";
	}

	//특정 게시물 번호에 대한 상세보기 처리
	@GetMapping({ "/get","/modify" })
	public void get(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criteria cri, Model model) {

		log.info("/get or modify");
		model.addAttribute("board", service.get(bno));
	}
	
	@PreAuthorize("principal.username == #board.writer")
	@PostMapping("/modify")
	public String modify(BoardVO board, @ModelAttribute("cri") Criteria cri,RedirectAttributes rttr) {
		log.info("modify: " + board);
		
		if(service.modify(board)) {
			//수정 처리후 success라는 문자열을 사용하기 위해 일회성 속성인 result를 선언
			rttr.addFlashAttribute("result", "success");
		}
		
		//게시물 리스트로 이동
		return "redirect:/board/list" + cri.getListLink();
	}
	
	// http://localhost:8080/board/remove?bno=xx
	@PreAuthorize("principal.username == #writer")
	@PostMapping("/remove")
	public String remove(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr, String writer) {
		log.info("remove.........." + bno);
		
		List<BoardAttachVO> attachList = service.getAttachList(bno);
		
		if(service.remove(bno)) {
			deleteFiles(attachList);
			//삭제 처리 성공여부를 일회성 redirectAttribute를 이용해 전송
			rttr.addFlashAttribute("result", "success");
		}
				
		//삭제 처리후 게시물 리스트로 이동
		return "redirect:/board/list" + cri.getListLink();
	}
	
	@GetMapping(value = "/getAttachList", 
				produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<BoardAttachVO>> getAttachList(Long bno){
		log.info("getAttachList " + bno);
		return new ResponseEntity<>(service.getAttachList(bno), HttpStatus.OK);
	}
	
	private void deleteFiles(List<BoardAttachVO> attachList) {
		if(attachList == null || attachList.size() == 0) {
			return;
		}
		
		log.info("delete attach files.................");
		log.info(attachList);
		
		attachList.forEach(attach ->{
			try {
				//삭제하려는 첨부파일의 경로를 통해 파일의 정보를 리턴(원본파일)
				Path file = Paths.get("C:\\upload\\" + attach.getUploadPath() + "\\" + attach.getUuid() + "_" + attach.getFileName());
				//삭제하려는 파일이 존재하면 삭제처리
				Files.deleteIfExists(file);
				
				//probeContentType : 파일의 MIME 형태를 리턴
				//image/jpeg, image/jpg, image/gif...
				//startsWith : JSTL function의 메서드
				if(Files.probeContentType(file).startsWith("image")) {
					//삭제하려는 파일이 썸네일 일때
					Path thumbNail = Paths.get("C:\\upload\\" + attach.getUploadPath() + "\\s_" + attach.getUuid() + "_" + attach.getFileName());
					
					Files.delete(thumbNail);
				}
			}catch(Exception e) {
				log.error("delete file error" + e.getMessage());
			}
		});
	}
	
	
}
