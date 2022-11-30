package org.fintech.controller;

import org.fintech.domain.Criteria;
import org.fintech.domain.ReplyPageDTO;
import org.fintech.domain.ReplyVO;
import org.fintech.service.ReplyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@RequestMapping("/replies/")
//REST방식의 컨트롤러 기능 수행을 알리는 어노테이션
@RestController
@Log4j
//모든 필드를 매개변수로 하는 생성자 자동 생성
@AllArgsConstructor
public class ReplyController {
	
	private ReplyService service;
	
	//http://localhost:8080/replies/new
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/new", 
				 //모든 클라이언트에서 json 형태로 요청이 오는것만 응답처리
				 consumes = "application/json",
				 //서버가 클라이언트에게 응답처리시 데이터를 지정된 마임으로 응답
				 produces = {MediaType.TEXT_PLAIN_VALUE})
	//ResponseEntity<> : 클라이언트에게 데이터 응답처리시 Htpp 상태 코드도 함께 처리
	//@RequestBody : Http 요청 Body 부분을 자바 객체로 전달하는 어노테이션
	public ResponseEntity<String> create(@RequestBody ReplyVO vo){
		log.info("ReplyVO: " + vo);
		int insertCount = service.register(vo);
		log.info("Reply INSERT COUNT: " + insertCount);
		
		//삼항 연산자 처리
		return insertCount == 1 
				? new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping(value = "/pages/{bno}/{page}",
				produces = {MediaType.APPLICATION_XML_VALUE,
							MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<ReplyPageDTO> getList(@PathVariable("page") int page,
												@PathVariable("bno") Long bno){
		Criteria cri = new Criteria(page, 10);
		log.info("get Reply List bno: " + bno);
		log.info("cri: " + cri);
		
		return new ResponseEntity<>(service.getListPage(cri, bno), HttpStatus.OK);
	}
	
	@GetMapping(value = "/{rno}",
				produces = {
					MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<ReplyVO> get(@PathVariable("rno") Long rno){
		log.info("get: " + rno);
		
		return new ResponseEntity<>(service.get(rno), HttpStatus.OK);
	}
	
	@PreAuthorize("principal.username == #vo.replyer")
	@DeleteMapping(value = "/{rno}", produces = { MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<String> remove(@RequestBody ReplyVO vo, @PathVariable("rno") Long rno) {

		log.info("remove: " + rno);
		
		log.info("replyer: " + vo.getReplyer());

		return service.remove(rno) == 1 
				? new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	@PreAuthorize("principal.username == #vo.replyer")
	@RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH}, 
					value = "/{rno}",
					consumes = "application/json")
	//클라이언트에서 전송되는 JSON 데이터를 ReplyVO 객체로 변환
	public ResponseEntity<String> modify(@RequestBody ReplyVO vo,
										 @PathVariable("rno") Long rno){
		vo.setRno(rno);
		
		log.info("rno: " + rno);
		log.info("modify: " + vo);
		
		return service.modify(vo) == 1
				? new ResponseEntity<>("success", HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	
}
