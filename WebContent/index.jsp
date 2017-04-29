<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1">

<title></title>
<c:set var="base" value="${pageContext.request.contextPath}"></c:set>
<link href="${base}/css/bootstrap.css" rel="stylesheet">
<script src="${base}/js/jquery-3.2.1.js"></script>
<script src="${base}/js/bootstrap.js"></script>

<!-- 1.引入文件: 引入这些文件至 <head> 中 -->
<!--[if lte IE 8]><link rel="stylesheet" href="${base}/css/nav/responsive-nav.css"><![endif]-->
<!--[if gt IE 8]><!-->
<link rel="stylesheet" href="${base}/css/nav/styles.css">
<!--<![endif]-->
<script src="${base}/js/nav/responsive-nav.js"></script>

</head>
<body>
	<div role="navigation" id="foo" class="nav-collapse">
		<ul>
			<li class="active"><a href="#">Home</a></li>
			<li><a href="#">About</a></li>
			<li><a href="#">Projects</a></li>
			<li><a href="#">Blog</a></li>
		</ul>
	</div>
	<div role="main" class="main">
		<div class="container">
			<div class="row">
				<a href="#nav" class="nav-toggle">Menu</a>
			</div>
			<!-- Bootstrap 超大屏幕（Jumbotron） -->
			<div class="row jumbotron">
				<h1>我的第一个 Bootstrap 页面</h1>
				<p>拖动窗口,查看改变</p>
			</div>
			<div class="row">
				<div class="col-sm-4">
					<h3>第一列</h3>
					<p>学的不仅是技术，更是梦想！</p>
					<p>再牛逼的梦想,也抵不住你傻逼似的坚持！</p>
				</div>
				<div class="col-sm-4">
					<h3>第二列</h3>
					<p>学的不仅是技术，更是梦想！</p>
					<p>再牛逼的梦想,也抵不住你傻逼似的坚持！</p>
				</div>
				<div class="col-sm-4">
					<h3>第三列</h3>
					<p>学的不仅是技术，更是梦想！</p>
					<p>再牛逼的梦想,也抵不住你傻逼似的坚持！</p>
				</div>
			</div>
		</div>
	</div>
	<!-- 3.启动此插件:将下面这段代码放置在 </body> 之前 -->
	<script>
		var navigation = responsiveNav("foo", {
			customToggle : ".nav-toggle"
		});
	</script>
</body>
</html>