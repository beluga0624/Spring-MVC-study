<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
		</div>
			<!-- /#page-wrapper -->
			
			</div>
			<!-- /#wrapper -->
			
			<!-- Bootstrap Core JavaScript -->
			<script src="/resources/vendor/bootstrap/js/bootstrap.min.js"></script>
			
			<!-- Metis Menu Plugin JavaScript -->
			<script src="/resources/vendor/metisMenu/metisMenu.min.js"></script>
			
			<!-- DataTables JavaScript -->
			<script src="/resources/vendor/datatables/js/jquery.dataTables.min.js"></script>
			<script src="/resources/vendor/datatables-plugins/dataTables.bootstrap.min.js"></script>
			<script src="/resources/vendor/datatables-responsive/dataTables.responsive.js"></script>
			
			<!-- Custom Theme JavaScript -->
			<script src="/resources/dist/js/sb-admin-2.js"></script>
			
			<!-- Page-Level Demo Scripts - Tables - Use for reference -->
			<script>
			$(document).ready(function() {
			    $('#dataTables-example').DataTable({
			        responsive: true
			    });
			    //모바일 크기에서 메뉴를 실행시 새로고침 버튼을 클릭하면 메뉴가
			    //자동으로 펼쳐지는 오류를 수정하기 위함
			    //sidebar-nav : 부트스트랩 네비게이션바 자동 접기 구현 클래스
			    //aria-expanded : 메뉴가 펼쳐지는 것을 false로 방지
			    $(".sidebar-nav")
			    	.attr("class", "sidebar-nav navbar-collapse collapse")
			    	.attr("aria-expanded", 'false')
			    	.attr("style", "height:1px");
			});
			</script>
		
	</body>
	
</html>