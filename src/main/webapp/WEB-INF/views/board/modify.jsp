<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@include file="../includes/header.jsp" %>

<style>
	.uploadResult {
		width: 100%;
		background-color: gray;
	}
	
	.uploadResult ul {
		display:flex;
		flex-flow: row;
		justify-content: center;
		align-items: center;
	}
	
	.uploadResult ul li {
		list-style: none;
		padding: 10px;
		align-content: center;
		text-align: center;
	}
	
	.uploadResult ul li span {
		color: white;
	}
	
	.bigPictureWrapper {
		position: absolute;
		display: none;
		justify-content: center;
		align-items: center;
		top: 0%;
		width: 100%;
		height: 100%;
		background-color: gray;
		z-index: 100;
		background: rgba(255,255,255,0.5);
	}
	
	.bigPicture {
		position: relative;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	
	.bigPicture img {
		width: 600px;
	}
</style>

	<div class="row">
	     <div class="col-lg-12">
	         <h1 class="page-header">Board Register</h1>
	     </div>
	     <!-- /.col-lg-12 -->
	 </div>
	 <!-- /.row -->
	 <div class="row">
	     <div class="col-lg-12">
	         <div class="panel panel-default">
	             <div class="panel-heading">
	                Board Register
	             </div>
	             <!-- /.panel-heading -->
	             <div class="panel-body">
	               
	                <form role="form" action="/board/modify" method="post">
	                	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	                	<input type="hidden" name="pageNum" value='<c:out value="${cri.pageNum}" />'>
						<input type="hidden" name="amount" value='<c:out value="${cri.amount}" />'>
						<input type="hidden" name="keyword" value='<c:out value="${cri.keyword}" />'>
						<input type="hidden" name="type" value='<c:out value="${cri.type}" />'>
	                
						<div class="form-group">
							<label>Bno</label>
							<input class="form-control" name='bno'
								value='<c:out value="${board.bno}"/>' readonly="readonly">
						</div>
						
						<div class="form-group">
							<label>Title</label>
							<input class="form-control" name='title'
								value='<c:out value="${board.title}"/>'>
						</div>
						
						<div class="form-group">
							<label>Text area</label>
							<textarea class="form-control" row="3" name="content"><c:out value="${board.content}"/></textarea>
						</div>
						
						<div class="form-group">
							<label>Writer</label>
							<input class="form-control" name='writer'
								value='<c:out value="${board.writer}"/>' readonly="readonly">
						</div>
						
						<div class="form-group">
							<!-- <label>RegDate</label> -->
							<input type="hidden" class="form-control" name='regdate'
								value='<fmt:formatDate pattern = "yyyy/MM/dd" value="${board.regdate}"/>' readonly="readonly">
						</div>
						
						<%-- <div class="form-group">
							<!-- <label>Update Date</label> -->
							<input type="hidden" class="form-control" name='updateDate'
								value='<fmt:formatDate pattern = "yyyy/MM/dd" value="${board.updateDate}"/>' readonly="readonly">
						</div> --%>
						
						<sec:authentication property="principal" var="pinfo"/>
	                	
	                	<sec:authorize access="isAuthenticated()">
	                		<c:if test="${pinfo.username eq board.writer}">
	                			<button type="submit" data-oper="modify" class="btn btn-success">Modify</button>
								<button type="submit" data-oper="remove" class="btn btn-danger">Remove</button>	
	                		</c:if>
	                	</sec:authorize>

						<button type="submit" data-oper="list" class="btn btn-info">List</button>
	              	</form>
	             </div>
	             <!-- /.panel-body -->
	         </div>
	         <!-- /.panel -->
	     </div>
	     <!-- /.col-lg-12 -->
	 </div>
	 <!-- /.row -->
	 
	 <div class="bigPictureWrapper">
	 	<div class="bigPicture">
	 	</div>
	 </div>
	 
	 <!-- 첨부파일 미리보기 -->
	 <div class="row">
	 	<div class="col-lg-12">
	 		<div class="panel panel-default">
	 			<div class="panel-heading">Files</div>
	 			<div class="panel-body">
					<div class="form-group uploadDiv">
						<input type="file" name="uploadFile" multiple="multiple">
					</div>	
	 				<div class="uploadResult">
	 					<ul>
	 						
	 					</ul>
	 				</div>
	 			</div>
	 		</div>
	 	</div>
	 </div>
	 
	<script type="text/javascript">
		$(document).ready(function(){
			(function(){
				var bno = '<c:out value="${board.bno}"/>';
				
				$.getJSON("/board/getAttachList", {bno: bno}, function(arr){
		 			console.log(arr);
		 			var str = "";
		 			
		 			$(arr).each(function(i, attach){
		 				//image type
		 				if(attach.fileType){
		 					var fileCallPath = encodeURIComponent(attach.uploadPath + "/s_" + attach.uuid + "_" + attach.fileName);
		 					
		 					str += "<li data-path='" + attach.uploadPath + "' data-uuid='" + attach.uuid + 
		 							"' data-filename='" + attach.fileName + "'data-type='" + attach.fileType + "'><div>";
							str += "<span> " + attach.fileName + "</span>";
							str += "<button type='button' data-file=\'" + fileCallPath + "\' data-type='image' ";
							str += "class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
		 					str += "<img src='/display?fileName=" + fileCallPath + "'>";
		 					str += "</div></li>"
		 				}else{
		 					str += "<li data-path='" + attach.uploadPath + "' data-uuid='" + attach.uuid + 
								"' data-filename='" + attach.fileName + "' data-type='" + attach.fileType + "'><div>";
							str += "<span> " + attach.fileName + "</span>";
							str += "<button type='button' data-file=\'" + fileCallPath + "\' data-type='image' ";
							str += "class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
							str += "<sapn>" + attach.fileName + "</span><br>"
							str += "<img src='/resources/img/attach.png'>";
							str += "</div></li>"
		 				}
		 			});
		 			$(".uploadResult ul").html(str);
		 		});
			})();
		});
		
		$(".uploadResult").on("click", "button", function(e){
			console.log("delete file");
			if(confirm("Remove this file? ")){
				var targetLi = $(this).closest("li");
				targetLi.remove();
			}
		});
	</script>
	 
	 <!-- 수정화면 버튼처리 -->
	 <script>
	 	$(document).ready(function(){
	 		var formObj = $("form");
	 		
	 		$('button').on("click", function(e){
	 			e.preventDefault();
	 			var operation = $(this).data("oper");
	 			console.log(operation);
	 			
	 			if(operation === 'remove'){
	 				formObj.attr("action", "/board/remove");
	 			}else if(operation === 'list'){
	 				formObj.attr("action", "/board/list").attr("method", "get");var pageNumTag = $("input[name='pageNum']").clone();
	 				var pageNumTag = $("input[name='pageNum']").clone(); // clone() : 선택한 요소를 복제
	 				var amountTag = $("input[name='amount']").clone();
	 				var keywordTag = $("input[name='keyword']").clone();
	 				var typeTag = $("input[name='type']").clone();
	 				
	 				//remove() : 태그와 값을 모두 삭제
	 				//empty() : 태그는 그대로 두고 값만 삭제
	 				formObj.empty();
	 				formObj.append(pageNumTag); //append(): formObj에 추가
	 				formObj.append(amountTag); //게시물 리스트로 이동시 현재 페이지 번호와 페이지당 행 수 속성값만 전달
	 				formObj.append(keywordTag);
	 				formObj.append(typeTag);
	 			//게시물 수정 버튼 클릭시 첨부파일 내역을 서버로 전송하기 위해 hidden 처리하여 전송
	 			}else if(operation === 'modify'){
	 				
	 				console.log("submit clicked");
	 				
	 				var str = "";
	 				
	 				$(".uploadResult ul li").each(function(i, obj){
	 					var jobj = $(obj);
	 					
	 					console.dir(jobj);

		 				str += "<input type='hidden' name='attachList[" + i + "].fileName' value='" + jobj.data("filename") + "'>";
		 				str += "<input type='hidden' name='attachList[" + i + "].uuid' value='" + jobj.data("uuid") + "'>";
		 				str += "<input type='hidden' name='attachList[" + i + "].uploadPath' value='" + jobj.data("path") + "'>";
		 				str += "<input type='hidden' name='attachList[" + i + "].fileType' value='" + jobj.data("type") + "'>";
	 				});
	 				
	 				formObj.append(str).submit();
	 			}
	 			formObj.submit();
	 		});
	 	});
	 	
	 	var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
 		var maxSize = 5242880;//5MB
 		
 		function checkExtension(fileName, fileSize){
 			if(fileSize >= maxSize){
 				alert("파일 사이즈 초과");
 				return false;
 			}
 			
 			if(regex.test(fileName)){
 				alert("해당 종류의 파일은 업로드할 수 없습니다.");
 				return false;
 			}
 			return true;
 		}
 		
 		var csrfHeaderName = "${_csrf.headerName}";
		var csrfTokenValue = "${_csrf.token}";
 		
 		//input 태그의 값이 변경되면 처리
 		$("input[type='file']").change(function(e){
 			//업로드 첨부파일 목록을 변수에 대입
 			var formData = new FormData();
 			var inputFile = $("input[name='uploadFile']");
 			//업로드 첨부파일이 FileList구조로 생성
 			var files = inputFile[0].files;
 			
 			for(var i=0; i<files.length; i++){
 				if(!checkExtension(files[i].name, files[i].size)){
 					return false;
 				}
 				//uploadFile 속성에 업로드 파일의 속성을 대입
 				formData.append("uploadFile", files[i]);
 			}
 			
 			$.ajax({
 				url: '/uploadAjaxAction',
 				processData: false,
 				contentType: false, 
 				data: formData,
 				type: "POST",
 				beforeSend: function(xhr){
 					xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
 				},
 				dataType: 'json',
 				success: function(result){
 					console.log(result);
 					
 					showUploadResult(result);
 				}
 			});
 		});
 		
 		function showUploadResult(uploadResultArr){
			//첨부파일이 없으면 리턴
 			if(!uploadResultArr || uploadResultArr.length == 0){
 				return;
 			}
 			
 			var uploadUL = $(".uploadResult ul");
 			var str = "";
 			
 			//매개변수로 전달된 첨부파일을 반복문을 통해 html 문을 생성
 			$(uploadResultArr).each(function(i, obj){
 				if(obj.image){//첨부파일이 이미지일때
 					var fileCallPath = encodeURIComponent(obj.uploadPath + "/s_" + obj.uuid + "_" + obj.fileName);
 					str += "<li data-path='" + obj.uploadPath + "'";
 					str += "data-uuid='" + obj.uuid + "' data-filename='" + obj.fileName + "' data-type='" + obj.image + "'><div>";
 					str += "<span>" + obj.fileName + "</span>";
 					str += "<button type='button' data-file=\'" + fileCallPath + "\' data-type='image' class='btn btn-warning btn-circle'>";
 					str += "<i class='fa fa-times'></i></button><br>";
 					str += "<img src='/display?fileName=" + fileCallPath + "'>";
 					str += "</div></li>"
 				}else{//첨부파일이 일반 파일일때
 					var fileCallPath = encodeURIComponent(obj.uploadPath + "/" +
 															obj.uuid + "_" + obj.fileName);
 					var fileLink = fileCallPath.replace(new RegExp(/||/g), "/");
 					str += "<li data-path='" + obj.uploadPath + "'";
 					str += " data-uuid='" + obj.uuid + "' data-filename='" + obj.fileName + "' data-type='" + obj.image/* java AttachFileDTO 클래스의 필드명과 같게 해야함 */ + "'><div>";
 					str += "<span>" + obj.fileName + "</span>"; 
 					str += "<button type='button'  data-file=\'" + fileCallPath + "\' data-type='file'  class='btn btn-warning btn-circle'>";
 					str += "<i class='fa fa-times'></i></button><br>";
 					str += "<img src='/resources/img/attach.png'></a>";
 					str += "</div></li>"
 					
 				}
 			});
 			uploadUL.append(str);
 		}
	 </script>
	 
<%@include file="../includes/footer.jsp" %>