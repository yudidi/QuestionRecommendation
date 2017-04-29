<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<c:set var="base" value="${pageContext.request.contextPath}"></c:set>
<link href="${base}/css/bootstrap.css" rel="stylesheet">
<script src="${base}/js/jquery-3.2.1.js"></script>
<script src="${base}/js/bootstrap.js"></script>

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

#header img.cloud{
margin:-3em 0 -2em 0;
}

</style>
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
					<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span> 
					<input id="myinput" type="text" class="form-control input-lg" " placeholder="请输入你在牛客网的ID">
					<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span> 
				</div>
			</div>
		</div>
		<div class="col-xs-1 col-sm-2 col-md-4"></div>
	</div>
</body>
</html>