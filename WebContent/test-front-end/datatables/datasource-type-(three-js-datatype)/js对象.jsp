<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<c:set var="base" value="${pageContext.request.contextPath}"></c:set>

<!-- DataTables CSS -->
<link rel="stylesheet" type="text/css" href="${base}/css/DataTables-1.10.15/jquery.dataTables.css">
<!-- jQuery  -->
<script type="text/javascript" charset="utf8" src="${base}/js/jquery-3.2.1.js"></script>
<!-- DataTables js -->
<script type="text/javascript" charset="utf8" src="${base}/js/DataTables-1.10.15/jquery.dataTables.js"></script>
</head>

<body>
	<table id="example" class="display" width="100%"></table>
</body>

<script type="text/javascript" charset="utf-8">
	var data = [ {
		"name" : "Tiger Nixon",
		"position" : "System Architect",
		"salary" : "$3,120",
		"start_date" : "2011/04/25",
		"office" : "Edinburgh",
		"extn" : "5421"
	}, {
		"name" : "Garrett Winters",
		"position" : "Director",
		"salary" : "$5,300",
		"start_date" : "2011/07/25",
		"office" : "Edinburgh",
		"extn" : "8422"
	} ];

	$(document).ready(function() {
		//object可以如下初始化表格
		$('#example').DataTable({
			data : data,
			//使用对象数组，一定要配置columns，告诉 DataTables 每列对应的属性
			//data 这里是固定不变的，name，position，salary，office 为你数据里对应的属性
			columns : [ {
				data : 'name'
			}, {
				data : 'position'
			}, {
				data : 'salary'
			}, {
				data : 'office'
			} ]
		});
	});
</script>
</html>