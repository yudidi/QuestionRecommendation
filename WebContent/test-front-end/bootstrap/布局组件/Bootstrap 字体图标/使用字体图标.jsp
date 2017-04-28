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
</head>
<body>
	<div class="container">
		<div class="jumbotron visible-md visible-lg">
			<h1>我的第一个 Bootstrap页面</h1>
			<h1>visible-md visible-lg</h1>
		</div>
		<div class="row">
			<div>
				<img alt="" src="http://localhost:8080/QuestionRecommendation/test-front-end/input/jQuery%E6%8F%92%E4%BB%B6%E5%BA%93%E6%8F%92%E4%BB%B6%E6%90%9C%E7%B4%A2_files/ggc.png">
			</div>
			<button type="button" class="btn btn-default btn-lg">
				<span class="glyphicon glyphicon-user"></span> User
			</button>
		</div>
	</div>
</body>
</html>