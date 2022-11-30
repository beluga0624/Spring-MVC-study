<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
	//bf cache 중지 03.21
	//no-cache(Http 1.0) ? 클라이언트가 요청시 매번 서버의 응답처리를 요구
	//한다는 내용 즉 캐시를 활용 안하는 것
	//no-store ? 캐시를 저장 안한다는 설정(Http 1.1)
	response.setHeader("Cache-Control","no-cache,no-store");
	response.setHeader("Pragma","no-cache");
	//캐시 유효시간 없다는 선언
	response.setDateHeader("Expires",-1);
%>

	<%@include file="../includes/header.jsp" %>
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">게시판 리스트</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Board List
                            <!-- p250 -->
                            <button id="regBtn" 
                            		type="button"
                            		class="btn btn-primary btn-xs pull-right">
                            	게시판 등록	
                            </button>
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <table width="100%" class="table table-striped table-bordered table-hover" id="dataList">
                                <thead>
                                    <tr>
                                        <th>#번호</th>
                                        <th>제목</th>
                                        <th>작성자</th>
                                        <th>작성일</th>
                                        <th>수정일</th>
                                    </tr>
                                </thead>
                                
                                <c:forEach var="board" 
                                		   items="${list}">
                                	<tr>
                                		<td>
                                			<c:out value="${board.bno}"/>
                                		</td>
                                		<!--
                                			 list.jsp에서 특정 게시물 제목을 클릭하면
                                			 상세보기 화면인 get.jsp로 이동 
                                		-->
                                		<td>
                                			<a class="move" href="<c:out value='${board.bno}'/>">
                                				<c:out value="${board.title}"/> <b>[<c:out value="${board.replyCnt}"/>]</b>
                                			</a>
                                		</td>
                                		<td>
                                			<c:out value="${board.writer}"/>
                                		</td>
                                		<td>
                                			<fmt:formatDate pattern="yyyy-MM-dd" 
                                						    value="${board.regdate}"/>
                                		</td>
                                		<td>
                                			<fmt:formatDate pattern="yyyy-MM-dd" 
                                						    value="${board.updateDate}"/>
                                		</td>	
                                	</tr>	   
                                
                                </c:forEach>
                      
                            </table>
                            
                            <!-- 검색 처리 추가 시작 03.15 -->
                            <div class="row">
                            	<div class="col-lg-12">
                            		<form id="searchForm" 
                            			  action="/board/list"
                            			  method="get">
                            			<select name="type">
                            				<option value=""    <c:out value="${pageMaker.cri.type == null ? 'selected':''}"/>>--</option>
                            				<option value="T"   <c:out value="${pageMaker.cri.type == 'T' ? 'selected':''}"/>>제목</option>
                            				<option value="C"   <c:out value="${pageMaker.cri.type == 'C' ? 'selected':''}"/>>내용</option>
                            				<option value="W"   <c:out value="${pageMaker.cri.type == 'W' ? 'selected':''}"/>>작성자</option>
                            				<option value="TC"  <c:out value="${pageMaker.cri.type eq 'TC' ? 'selected':''}"/>>제목 or 내용</option>
                            				<option value="TW"  <c:out value="${pageMaker.cri.type eq 'TW' ? 'selected':''}"/>>제목 or 작성자</option>
                            				<option value="TWC" <c:out value="${pageMaker.cri.type eq 'TWC' ? 'selected':''}"/>>제목 or 내용 or 작성자</option>
                            			</select>
                            			<input type="text" 
                            				   name="keyword"
                            				   style="text-transform:uppercase;"
                            				   value='<c:out value="${pageMaker.cri.keyword}"/>'/>
                            				   
                            			<input type="hidden" name="pageNum" value='<c:out value="${pageMaker.cri.pageNum}"/>'/>
                            			<input type="hidden" name="amount" value='<c:out value="${pageMaker.cri.amount}"/>'/>
                            			<button class="btn btn-primary">Search</button>
                            		</form>
                            	</div>
                            </div>
                            <!-- 검색 처리 추가 종료 03.15 -->
                            
                            <!-- 페이징 처리 추가 시작 03.14 -->
                            <!-- 
                            	pull-right:우측정렬
                            	pagination :페이징 아이콘 표시 
                            -->
                            <div class="pull-right">
                            	<ul class="pagination">
                            		<!-- 이전 페이지 처리-->
                            		<c:if test="${pageMaker.prev}">
                            			<li class="paginate_button previous">
                            				<a href="${pageMaker.startPage-1}">이전</a>
                            			</li>
                            		</c:if>
                            		
                            		<!-- 하단에 10개의 페이지번호 처리-->
									<c:forEach var="num" 
									           begin="${pageMaker.startPage}" 
									           end="${pageMaker.endPage}">
                            			<li class="paginate_button ${pageMaker.cri.pageNum == num ? 'active':''}">
                            				<a href="${num}">${num}</a>
                            			</li>
                            		</c:forEach>                            		
                            		
                            		<!-- 다음 페이지 처리-->
                            		<c:if test="${pageMaker.next}">
                            			<li class="paginate_button next">
                            				<a href="${pageMaker.endPage+1}">다음</a>
                            			</li>
                            		</c:if>                            		
                            	</ul>
                            </div>
                            <!-- 페이징 처리 추가 종료 03.14 -->
                            
                            <!-- p311 코딩 -->
                            <form id="actionForm" 
                            	  action="/board/list" method="get">
                            	  <input type="hidden" name="pageNum" value="${pageMaker.cri.pageNum}">
                            	  <input type="hidden" name="amount" value="${pageMaker.cri.amount}">
					              
					              <!-- 
					              	선택한 게시물 조건과 단어를 페이지 이동후에도 선택되게 처리
					              	하기 위해서 선언 03.15 
					              -->              	  
                            	  <input type="hidden" name="type" value='<c:out value="${pageMaker.cri.type}"/>'>
                            	  <input type="hidden" name="keyword" value='<c:out value="${pageMaker.cri.keyword}"/>'>
                            </form>
                            
                            <!-- 모달창 시작 03.11-->
                            <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                            	<div class="modal-dialog">
                            		<div class="modal-content">
                            			<div class="modal-header">
                            				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                            					&times;
                            				</button>
                            				<h4 class="modal-title" id="myModalLabel">Model Title</h4>
                            			</div>
                            			<div class="modal-body">
                            				처리가 완료되었습니다.
                            			</div>
                            			<div class="modal-footer">
                            				<button type="button" class="btn btn-success" data-dismiss="modal">
                            					Close
                            				</button>
                            				<button type="button" class="btn btn-primary" data-dismiss="modal">
                            					Save Changes
                            				</button>
                            			</div>
                            		</div>
                            	</div>
                            </div>
                            <!-- 모달창 종료 03.11-->

                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-lg-12 -->
            </div>
        </div>
 <%@include file="../includes/footer.jsp" %>
 
 <script>
 	$(document).ready(function(){
 		$("#dataList").DataTable({
 			"paging":false,
 			"ordering":false,
 			"info":false,
 			"searching":false
 		});
 		
 		//신규 게시물 등록할때 게시물 번호를 가져온다.
 	 	var result = '<c:out value="${result}"/>';
 	 	
 	 	//함수에 매개변수로 전달
 	 	checkModal(result);
 	 	
 	 	//03.14 뒤로가기시 문제 해결
 	 	//history.replaceState?
 	 	//이전주소값을 새로운 url로 변경처리
 	 	//첫번째 매개변수:history.state에 기록할 데이터
 	 	//두번째 매개변수:타이틀
 	 	//세번째 매개변수:새로운 url 주소
 	 	history.replaceState({},null,null);
 	 	
 	 	function checkModal(result){
 	 		//모달창을 안보이게 처리
 	 		if(result === "" || history.state){//데이터타입비교 &데이터도 비교
 	 			return;
 	 		}
 	 		
 	 		//리턴된 게시물 번호가 존재하는 경우
 	 		//class로 지정된 modal-body부분에 알림문구 출력
 	 		if(parseInt(result) > 0){
 	 			$(".modal-body").html("게시글 " + parseInt(result) + " 번이 등록되었습니다.");
 	 		}
 	 		
 	 		//모달창을 보여준다.
 	 		$("#myModal").modal("show");
 	 	}
 	 	
 	 	//03.14 게시판등록 버튼 처리
 	 	$("#regBtn").on("click",function(){
 	 		//게시판 등록 화면으로 이동
 	 		self.location = "/board/register";
 	 	});
 	 	
 	 	//p312 페이지 번호 클릭시 처리
 	 	var actionForm = $("#actionForm");
 	 	
 	 	$(".paginate_button a").on("click",function(e){
 	 		e.preventDefault();//a 태그가 실행되는 것을 방지한다.
 	 		
 	 		actionForm.find("input[name='pageNum']").val($(this).attr("href"));
 	 		
 	 		actionForm.submit();
 	 		
 	 	});
 	 	
 	 	//p315 코딩
 	 	$(".move").on("click",function(e){
 	 		e.preventDefault();
 	 		actionForm.append("<input type='hidden' name='bno' value='"+$(this).attr("href") + "'>");
 	 		actionForm.attr("action","/board/get");
 	 		actionForm.submit();
 	 	});
 	 	
 	 	//p342 코딩
 	 	var searchForm = $("#searchForm");
 	 	
 	 	$("#searchForm button").on("click",function(e){
 	 		if(!searchForm.find("option:selected").val()){
 	 			alert("검색조건을 선택하세요");
 	 			return false;
 	 		}
 	 		
 	 		if(!searchForm.find("input[name='keyword']").val()){
 	 			alert("키워드를 입력하세요");
 	 			return false;
 	 		}
 	 		
 	 		//검색시 페이지번호는 1페이지로 지정
 	 		searchForm.find("input[name='pageNum']").val("1");
 	 		
 	 		e.preventDefault();
 	 		
 	 		searchForm.submit();
 	 		
 	 	});
 	 	
 		
 	});
 </script>
 
 
 
 
 
 
 
 
 
 
 
 
 
        
        