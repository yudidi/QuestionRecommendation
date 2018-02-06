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

<table id="example" class="display cell-border" cellspacing="0" width="100%">
        <thead>
            <tr>
                <th>Name</th>
                <th>Position</th>
                <th>Office</th>
                <th>Extn.</th>
                <th>Start date</th>
                <th>Salary</th>
            </tr>
        </thead>
        <tfoot>
            <tr>
                <th>Name</th>
                <th>Position</th>
                <th>Office</th>
                <th>Extn.</th>
                <th>Start date</th>
                <th>Salary</th>
            </tr>
        </tfoot>
    </table>

<script type="text/javascript" charset="utf-8">
		$(document).ready(function() {
		    $('#example').DataTable( {
		        "ajax": '${base}/recommend',
		        "dom": '<f<t>ip>'
		    } );
		} );
</script>
</html>