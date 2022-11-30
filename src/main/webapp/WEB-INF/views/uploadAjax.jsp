<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Insert title here</title>
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
				backround: rgba(255,255,255,0.5);
			}
			
			.bigPicture{
				position: relative;
				display: fles;
				justify-content: center;
				align-items: center;
			}
			
			bigPicture img {
				width: 600px;
			}
		</style>
	</head>
	<body>
		<div class="uploadDiv">
			<input type="file" name="uploadFile" multiple>
		</div>
		
		<div class="uploadResult">
			<ul>
			</ul>
		</div>
		
		<div class="bigPictureWrapper">
			<div class="bigPicture"></div>
		</div>
		
		<button id="uploadBtn">Upload</button>

		<script src="http://code.jquery.com/jquery-latest.min.js"></script>
		<script>
			
			function showImage(fileCallPath){
				$(".bigPictureWrapper").css("display", "flex").show();
				
				$(".bigPicture").html("<img src='/display?fileName=" + encodeURI(fileCallPath) + "'>")
					.animate({width:"100%", height: "100%"}, 1000);
				
				//출력된 원본 이미지를 안보이게 처리
				$(".bigPictureWrapper").on("click", function(e){
					$(".bigPicture").animate({width:"0", height: "0%"}, 1000);
					setTimeout(() => {
						$(this).hide();
					}, 1000);
				});
			}
		
			$(document).ready(function(){
				
				//업로드하는 파일의 확장자 정규식 선언
				var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
				var maxSize = 5242880; //5MB
				
				function checkExtension(fileName, fileSize){
					if(fileSize >= maxSize){
						alert("파일 사이즈 초과");
						return false;
					}
					if(regex.test(fileName)){
						alert("해당 종류의 파일은 업로드 할 수 없습니다.");
						return false;
					}
					return true;
				}
				
				//class로 지정된 uploadDiv 영역 복제
				var cloneObj = $(".uploadDiv").clone();
				
				$("#uploadBtn").on("click", function(e){
					 
					//<form> 태그와 같은 효과, key와 value를 저장하는 객체로 데이터를 서버로 전송하는 효과가 있다 
					var formData = new FormData();
					//업로드한 파일명들을 변수에 대입
					var inputFile = $("input[name='uploadFile']");
					var files = inputFile[0].files;
					
					console.log(files);
					
					for(var i=0; i<files.length; i++){
						
						//정규식에 맞지 않거나 파일 사이즈가 5MB를 넘는 경우 
						if(!checkExtension(files[i].name, files[i].size)){
							return false;
						}
						//formData에 append를 수행하면 해당하는 키가 없으면 생성하고 있으면 추가
						formData.append("uploadFile", files[i]);
					}
					
					//processData : formData로 전달된 매개변수들을 jquery 내부적으로 
					//				query문자열로 변환하는 처리를 못하게 false처리
					//contentType : 기본값이 application/x-www-form-unlencoded;utf-8 이므로
					//				이렇게 처리되면 안되고 multipart/form-data로 처리되게 하기위해 false
					$.ajax({
						url: '/uploadAjaxAction',
							processData: false,
							contentType: false,
							data: formData,
							type:'POST',
							dataType: 'json',
							success: function(result){

								console.log(result);
								
								showUploadedFile(result);
								//파일 업로드 처리후 화면 초기화
								//파일 업로드전 class로 지정된 부분을 복제해두고 다시 실행하면 화면 클리어 효과
								$(".uploadDiv").html(cloneObj.html());
								
							}
					});
					
				});
				
				$(".uploadResult").on("click", "span", function(e){
					var targetFile = $(this).data("file");
					var type = $(this).data("type");
					console.log(targetFile);
					
					$.ajax({
						url: '/deleteFile',
						data: {fileName: targetFile, type: type},
						dataType: 'text',
						type: 'POST',
						success: function(result){
							alert(result);
						}
					});
				});
				
				var uploadResult = $(".uploadResult ul");

				//첨부파일을 출력하는 함수
				function showUploadedFile(uploadResultArr){
					var str = "";
					//베열구조의 값을 가져오기 위한 반복 처리
					$(uploadResultArr).each(function(i, obj){
						
						//일반 파일 업로드 시
						if(!obj.image){
							var fileCallPath = encodeURIComponent(obj.uploadPath + 
									"/" + obj.uuid + "_" + obj.fileName);
							
							var fileLink = fileCallPath.replace(new RegExp(/\\/g), "/");
							
							// 첨부파일이 일반 파일인 경우 파일명 클릭시 다운로드 처리
							str += "<li><div><a href='/download?fileName=" + fileCallPath + "'>"
									+ "<img src='/resources/img/attach.png'>"
									+ obj.fileName + "</a><span data-file=\'" + fileCallPath + "\' data-type='file'> x </span></div></li>";
						}else{
							//한글을 포함한 파일을 업로드시 컴퓨터가 컴파일 할 수 있도록 바이트 코드로 변환하는 과정을 인코딩이라고 한다.
							var fileCallPath = encodeURIComponent(obj.uploadPath + "/s_"
																	+ obj.uuid + "_" + obj.fileName);
							
							//썸네일 이미지를 클릭시 원본 이미지 파일명 가져오기
							var originPath = obj.uploadPath + "\\" + obj.uuid + "_" + obj.fileName;
							
							// RegExp(/\\/g, "/") : 모든 \\를 찾아서 /로 대체
							// a 태그에 적용해야 하기 때문에 변환처리
							originPath = originPath.replace(new RegExp(/\\/g), "/");
							
							str += "<li><a href=\"javascript:showImage(\'" + originPath + "\')\"><img src='/display?fileName=" + fileCallPath + "'></a>" +
									"<span data-file=\'" + fileCallPath + "\' data-type='image'> x </span></li>";
						}
					});
					
					//셍성된 html 문자열을 결과 div 영역에 보여준다.
					uploadResult.append(str);
				}
			
			});
		
		</script>
		
	</body>
</html>