package org.fintech.controller;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
//Controller 및 웹 환경에 사용되는 객체들을 자동으로 Bean등록 처리를 해주는 어노테이션
@WebAppConfiguration
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring/root-context.xml", 
						"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})
@Log4j
public class BoardControllerTests {
	
	@Setter(onMethod_ = {@Autowired})
	private WebApplicationContext ctx;
	//Web application을 tomcat server를 이용하여 주소창에서 url을 처리하는게 아니라
	//프로그램에서 url을 처리할 수 있도록 하는 객체
	private MockMvc mockMvc;
	
	@Before
	public void setup() {
		//실행전 MockMvc 사용을 위한 준비 선언
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}
	
	@Ignore
	public void test() throws Exception{
		//perform : 결과로 ResultActions 객체를 리턴받는다.
		//MockMvcRequestBuilders.전송방식(실행 URL)
		//andReturn() : 테스트한 결과를 객체형식으로 받는 경우 선언
		//getModelAndView() : 요청을 전송하는 view값과 model 객체에 저장된 값을 리턴
		//getModelMap() : 리턴되는 값을 Map 형태로 받는다.
		log.info(mockMvc.perform(MockMvcRequestBuilders.get("/board/list"))
				 .andReturn().getModelAndView().getModelMap());
	}
	
	@Ignore
	public void testRegister() throws Exception {
		String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/board/register")
											.param("title", "테스트 새글 제목")
											.param("content", "테스트 새글 내용")
											.param("writer", "user00")
											).andReturn().getModelAndView().getViewName();
		
		log.info(resultPage);
	}
	
	@Ignore
	public void testGet() throws Exception {
		log.info(mockMvc.perform(MockMvcRequestBuilders.get("/board/get")
				 .param("bno", "2"))
				 .andReturn()
				 .getModelAndView().getModelMap());
	}
	
	@Ignore
	public void testModify() throws Exception {
		String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/board/modify")
											.param("bno", "10")
											.param("title", "수정된 테스트 새글 제목")
											.param("content", "수정된 테스트 새글 내용")
											.param("writer", "user00"))
							.andReturn().getModelAndView().getViewName();
		
		log.info(resultPage);
	}
	
	@Ignore
	public void testRemove() throws Exception{
		String resultPage = mockMvc.perform(MockMvcRequestBuilders.post("/board/remove")
											.param("bno", "7")
											).andReturn().getModelAndView().getViewName();
		
		log.info(resultPage);
	}
	
	@Test
	public void testListPaging() throws Exception {
		log.info(mockMvc.perform(MockMvcRequestBuilders.get("/board/list")
									.param("pageNum", "1")
									.param("amount", "20"))
									.andReturn().getModelAndView().getModelMap());
	}
	
}
