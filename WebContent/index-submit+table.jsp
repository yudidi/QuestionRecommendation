<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<c:set var="base" value="${pageContext.request.contextPath}"></c:set>
<link href="${base}/css/bootstrap.css" rel="stylesheet">
<!-- jQuery  -->
<script type="text/javascript" charset="utf8" src="${base}/js/jquery-3.2.1.js"></script>
<script src="${base}/js/bootstrap.js"></script>

<!-- DataTables CSS -->
<link rel="stylesheet" type="text/css" href="${base}/css/DataTables-1.10.15/jquery.dataTables.css">
<!-- DataTables js -->
<script type="text/javascript" charset="utf8" src="${base}/js/DataTables-1.10.15/jquery.dataTables.js"></script>

<style type="text/css">
/* header背景 */
#header {
	padding: 9em 0 9em 0;
	background-color: #4686a0;
	color: rgba(255, 255, 255, 0.75);
	background-attachment: fixed, fixed, fixed;
	background-image: url("img/backgroud/overlay2.png"),
		url("img/backgroud/overlay3.svg"),
		linear-gradient(45deg, #9dc66b 5%, #4fa49a 30%, #4361c2);
	background-position: top left, center center, center center;
	background-size: auto, cover, cover;
	overflow: hidden;
	position: relative;
	text-align: center;
}

#header {
	padding: 5em 0 5em 0;
}

/* 输入框的高度 */
#myinput {
	height: 60px;
	/* padding-left: 50px;  框内提示的文字左移50px; */
}

/* header各部分的距离调整 */
#header h1 {
	margin: 0 0 1 em 0;
	!
	important;
}

#header img.cloud {
	margin: -3em 0 -2em 0;
}
</style>

<style type="text/css">
#submit:HOVER {
	background-color: cornflowerblue;
}
</style>
<script type="text/javascript">
	function submit() {
		alert(this.Text);
	}
</script>
</head>
<body>
	<div class="container-fluid">
		<div id="header" class="row">
			<div class="col-xs-1 col-sm-2 col-md-4"></div>
			<div class="inner col-xs-10 col-sm-8 col-md-4">
				<img class="cloud" alt="白云" src="img/backgroud/cloud.png">
				<h1>
					Hi nowcoder, I'll serve you <br>
				</h1>
				<div class="input-group">
					<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span> <input id="myinput" type="text" class="form-control input-lg" placeholder="Input your nowcoder ID:5933350"> <span id="submit" onclick="submit(this)" class="input-group-addon"><i
						class="glyphicon glyphicon-circle-arrow-right"
					></i></span>
				</div>
			</div>
			<div class="col-xs-1 col-sm-2 col-md-4"></div>
		</div>
		<div style="height: 2em"></div>
		<div class="row">
			<div class="col-xs-1 col-sm-2 col-md-3"></div>
			<div class="col-xs-10 col-sm-8 col-md-6">
				<table id="example" class="display" cellspacing="0" width="100%">
					<thead class="" id="jumbotron">
						<tr>
							<th>Recommendation Results(<=3)</th>
						</tr>
					</thead>
				</table>
			</div>
			<div class="col-xs-1 col-sm-2 col-md-3"></div>
		</div>
	</div>

	<script type="text/javascript" charset="utf-8">
	
		$(document).ready(function() {
			$('#example').DataTable({
				"dom" : '<fl<t>ip>'
			});
		});

		function submit() {
			var uid = $("#myinput").val();
			var z= /^\d+$/;;
			if(z.test(uid) == false){
			   alert("不是数字");
			   return;
			};
			$('#example').DataTable({
				"destroy": true, 
				ajax : {
					type : 'post',
					url : '${base}/recommend',
					data : {"uid":uid},
					contentType: "application/x-www-form-urlencoded; charset=utf-8"
				},
				"columns" : [ {
					"data" : "subject"
				}, ]
			});
		}
	</script>

</body>
</html>