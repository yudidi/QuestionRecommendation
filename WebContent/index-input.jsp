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
	<div class="container-fluid">
		<div class="row">
		<div class="input-group">
             <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i> NewCoder</span>
            <input type="text" class="form-control input-lg"" placeholder="请输入你在牛客网的ID">
        </div>
		</div>
	</div>

</body>
</html>