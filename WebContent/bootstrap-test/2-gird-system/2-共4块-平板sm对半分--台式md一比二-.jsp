<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>2个列,每列2个段落</title>
<c:set var="base" value="${pageContext.request.contextPath}"></c:set>
<link href="${base}/css/bootstrap.css" rel="stylesheet">
<script src="${base}/js/jquery-3.2.1.js"></script>
<script src="${base}/js/bootstrap.js"></script>
<style type="text/css">
p {
	background-color: #f0f;
	border: 1 solid;
}
</style>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-xs-12">
				<h1 style="text-align: center;">Hello, world!</h1>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-6 col-md-4">
				<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
				<p>Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.</p>
			</div>
			<div class="col-sm-6 col-md-8">
				<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
				<p>Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.</p>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">http://www.runoob.com/bootstrap/bootstrap-grid-system-example2.html</div>
		</div>
	</div>
</body>
</html>