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
					<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span> <input id="myinput" type="text" class="form-control input-lg" placeholder="Input your nowcoder ID:5933350">
					 <span id="submit" onclick="submit()" class="input-group-addon"> <i
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
			$("#submit").hide();
			loadJson(uid);
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
				"columns" : [ {
					"data" : "subject"
				}, ]
			});
		}

		function loadJson(uid) {
			$('#example')
			.on('xhr.dt', function ( e, settings, json, xhr ) {
				$("#submit").show();
		    } )
			.DataTable({
				"destroy" : true,
				"dom" : '<<t>ip>',
				"language" : {
					"loadingRecords" : "Please wait - loading..."
				},
				"columns" : [ {
					"data" : "subject"
				}, ],
				ajax : {
					type : 'post',
					url : '${base}/recommend',
					async : true,
					data : {
						"uid" : uid
					},
					contentType : "application/x-www-form-urlencoded; charset=utf-8",
				}
			});
		}
	</script>

</body>
</html>