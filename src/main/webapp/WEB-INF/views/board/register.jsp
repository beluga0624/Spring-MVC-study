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
				display: flex;
				flex-flow: row;
				justify-content: center;
				align-items: center;
			}
			
			.uploadResult ul li {
				list-style: none;
				padding: 10px;
			}
			
			.uploadResult ul li img {
				width: 100px;
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
	                
	                <form role="form" action="/board/register" method="post">
	                	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	                
	                	<div class="from-group">
	                		<label>Title</label>
	                		<input class="form-control" name="title">
	                	</div>
	                	
	                	<div class="from-group">
	                		<label>Text area</label>
	                		<textarea class="form-control" rows="3" name="content"></textarea>
	                	</div>
	                	
	                	<div class="from-group">
	                		<label>Writer</label>
	                		<input class="form-control" name="writer" value='<sec:authentication property="principal.username" />' readonly="readonly" />
	                	</div>
	                	
	                	<button type="submit" class="btn btn-success">Submit Button</button>
	                	<button type="reset" class="btn btn-default">Reset Button</button>
	                	
	                </form>
	                
	             </div>
	             <!-- /.panel-body -->
	         </div>
	         <!-- /.panel -->
	     </div>
	     <!-- /.col-lg-12 -->
	 </div>
	 <!-- /.row -->
	 
	 <div class="row">
	 	<div class="col-lg-12">
	 		<div class="panel panel-default">
	 			<div class="panel-heading">File Attach</div>
	 			<div class="panel-body">
	 				<div class="from-group uploadDiv">
	 					<input type="file" name="uploadFile" multiple>
	 				</div>
	 				
	 				<div class="uploadResult">
	 					<ul>
	 						
	 					</ul>
	 				</div>
	 			</div>
	 		</div>
	 	</div>
	 </div>
	 <!-- 게시물 등록 버튼 처리 -->
	 <script>
	 	$(document).ready(function(e){
	 		//폼 전체 요소의 값을 변수에 대입
	 		var formObj = $("form[role='form']");
	 		
	 		//게시판 등록 버튼을 클릭하면
	 		$("button[type='submit']").on("click", function(e){
	 			e.preventDefault();
	 			console.log("submit clicked");
	 			var str = "";
	 			
	 			//업로드한 첨부파일 내역을 BoardVO.java 클래스의 attachList필드에 매핑하기위해 
	 			//게시물 등록버튼을 클릭하면 attachList배열에 모든 첨부파일 내역을 hidden 속성을 이용해 모두 전송
	 			$(".uploadResult ul li").each(function(i, obj){
	 				var jobj = $(obj);
	 				
	 				console.dir(jobj);
	 				
	 				str += "<input type='hidden' name='attachList[" + i + "].fileName' value='" + jobj.data("filename") + "'>";
	 				str += "<input type='hidden' name='attachList[" + i + "].uuid' value='" + jobj.data("uuid") + "'>";
	 				str += "<input type='hidden' name='attachList[" + i + "].uploadPath' value='" + jobj.data("path") + "'>";
	 				str += "<input type='hidden' name='attachList[" + i + "].fileType' value='" + jobj.data("type") + "'>";
	 			});
	 			
	 			formObj.append(str).submit();
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
	 				//xhr 헤더를 포함해서 Http Request를 수행하기 전 실행, 여기서 xhr은 XMLHttpRequest의 약자로
	 				//http 프로토콜을 통해 서버에 자원을 요청하기 위해 사용
	 				beforeSend: function(xhr){
	 					xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
	 				},
	 				data: formData,
	 				type: "POST",
	 				dataType: 'json',
	 				success: function(result){
	 					console.log(result);
	 					
	 					showUploadResult(result);
	 				}
	 			});
	 		});

	 		
	 		$(".uploadResult").on("click", "button", function(e){
	 			console.log("delete file");
	 			
	 			//경로를 포함한 이미지
	 			var targetFile = $(this).data("file");
	 			//첨부파일이 이미지이면 image, 일반 파일이면 file
	 			var type = $(this).data("type");
	 			// x 버튼에서 가장 가까운 li 태그
	 			var targetLi = $(this).closest("li");
	 			
	 			$.ajax({
	 				url: '/deleteFile',
	 				data: {fileName: targetFile, type: type},
	 				beforeSend: function(xhr){
	 					xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
	 				},
	 				dataType: 'text',
	 				type: "POST",
	 				success: function(result){
	 					alert(result);
	 					//태그와 값을 동시에 삭제처리 
	 					targetLi.remove();
	 				}
	 			});
	 			
	 		});
	 		
	 		//첨부파일 리스트 보여주기 처리
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
	 		
	 	});
	 	
	 </script>
	 
<%@include file="../includes/footer.jsp" %>