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

<!-- JQuery validate js -->
<script src="http://static.runoob.com/assets/jquery-validation-1.14.0/dist/jquery.validate.min.js"></script>
<!-- Header CSS -->
<link href="${base}/css/index.css" rel="stylesheet">

<script type="text/javascript">
	function submit() {
		alert(this.Text);
	}
</script>

<style type="text/css">
/* dataTables表头居中 */
#example>thead:first-child>tr:first-child>th {
	text-align: center;
}

/* 红色提示信息 */
.warning {
	color: red;
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
					<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span> <input id="myinput" type="text" class="form-control input-lg" placeholder="Input your nowcoder ID:5933350"> <span id="submit" onclick="submit(this)" class="input-group-addon"> <i
						class="glyphicon glyphicon-circle-arrow-right"
					></i>
					</span>
				</div>
			</div>
			<div class="col-xs-1 col-sm-2 col-md-4"></div>
		</div>
		<div class="row">
			<div class="col-xs-1 col-sm-2 col-md-3"></div>
			<div class="col-xs-10 col-sm-8 col-md-6">
				<table id="example" class="display" cellspacing="0" width="100%">
					<thead class="" id="jumbotron">
						<tr>
							<th>Recommendation Results</th>
						</tr>
					</thead>
					<tbody id="myTbody">
					</tbody>
				</table>
			</div>
			<div class="col-xs-1 col-sm-2 col-md-3"></div>
		</div>
		<div style="height: 2em"></div>
	</div>

	<script type="text/javascript" charset="utf-8">
		$(document).ready(function() {
			$('#example').DataTable({
				"dom" : '<<t>i>'
			});
		});

		function submit() {
			var uid = $.trim($("#myinput").val());
			if (checkInput(uid) == 0) {
				return;
			}
			startReporter();
			loadJson(uid);
			clearTimeout(t);
		}

		function checkInput(uid) {
			var z = /^\d+$/;
			var errorMsg = '';
			if (uid.length == 0) {
				errorMsg = [ {
					"subject" : '<label class="warning">Nowcoder ID is required.</label>'
				} ];
				loadErrorMsg(errorMsg);
				return 0;
			}
			if (z.test(uid) == false) {
				errorMsg = [ {
					"subject" : '<label class="warning">'
							+ uid
							+ '</label> is invalid, nowcoder ID should be pure numbers.'
				} ];
				loadErrorMsg(errorMsg);
				return 0;
			}
		}

		function loadErrorMsg(errorMsg) {
			$('#example').DataTable({
				"destroy" : true,
				"dom" : '<<t>ip>',
				data : errorMsg,
				//使用对象数组，一定要配置columns，告诉 DataTables 每列对应的属性
				//data 这里是固定不变的，name，position，salary，office 为你数据里对应的属性
				"columns" : [ {
					"data" : "subject"
				}, ]
			});
		}

		function loadJson(uid) {
			var data = getJson(uid);
			$('#example')
				.DataTable(
					{
						"destroy" : true,
						"dom" : '<<t>ip>',
						"data": data,
						"columns" : [ {
							"data" : "subject"
						}, ]
					});
		}
		
		var c=0;
		var t;
		function startReporter() {
			c = 0;
			timedCount();
		}
		
		function timedCount()
		{
			c = c+1;
			$("#example tbody").html("<tr><td>"+c+"s</td></tr>");
			t=setTimeout("timedCount()",1000);
		}
		
		function getJson(uid) {
			$.ajax({
				url:'${base}/recommend',
				method:'post',
				data:{'uid':uid},
				async:true,//false代表只有在等待ajax执行完毕后才执行
				success:function(result){
				alert(result);
				return result;
				}
			});
			return [ {
				"subject" : '<label class="warning">Error,please try again.</label>'
			} ];
		}
	</script>

</body>
</html>