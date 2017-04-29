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

<!--第二步：添加如下 HTML 代码-->
<table id="table_id_example" class="display">
	<thead>
		<tr>
			<th>Column 1</th>
			<th>Column 2</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>Row 1 Data 1</td>
			<td>Row 1 Data 2</td>
		</tr>
		<tr>
			<td>Row 2 Data 1</td>
			<td>Row 2 Data 2</td>
		</tr>
	</tbody>
</table>

<script type="text/javascript" charset="utf-8">
<!--第三步：初始化Datatables-->
	$(document).ready(function() {
		$('#table_id_example').DataTable();
	});
</script>
</html>