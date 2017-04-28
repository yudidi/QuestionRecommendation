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
	<table id="example" class="display" width="100%">
		<thead>
			<tr role="row">
				<th>Name</th>
				<th>Position</th>
				<th>Office</th>
				<th>Salary</th>
			</tr>
		</thead>
	</table>
</body>

<script type="text/javascript" charset="utf-8">
function Employee ( name, position, salary, office ) {
    this.name = name;
    this.position = position;
    this.salary = salary;
    this._office = office;

    this.office = function () {
        return this._office;
    }
};

	$(document).ready(function() {
		$('#example').DataTable( {
	        data: [
	            new Employee( "Tiger Nixon", "System Architect", "$3,120", "Edinburgh" ),
	            new Employee( "Garrett Winters", "Director", "$5,300", "Edinburgh" )
	        ],
	        columns: [
	            { data: 'name' },
	            { data: 'salary' },
	            { data: 'office()' },
	            { data: 'position' }
	        ]
	    } );
	});
</script>
</html>