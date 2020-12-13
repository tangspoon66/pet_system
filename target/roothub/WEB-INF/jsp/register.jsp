<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<title>宠物信息服务平台-注册</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- 引入 Bootstrap -->
<link href="/resources/css/bootstrap.min.css" rel="stylesheet">
<link href="/resources/css/app.css" rel="stylesheet" type="text/css">
<link rel="shortcut icon" href="/resources/images/favicon.ico">
</head>
<body>
	<div class="wrapper">
		<jsp:include page="components/head.jsp"></jsp:include>
		<div class="row">
			<div class="col-md-9">
				<div class="panel panel-default">
					<div class="panel-heading">
						<a href="/">主页</a> / 注册
					</div>
					<div class="panel-body">
						<form role="form" id="form" method="post">
							<div class="form-group">
								<label for="username">用户名</label> <input type="text"
									class="form-control" id="username" name="username"
									placeholder="用户名,长度2-8位">
							</div>
							<div class="form-group">
								<label for="password">密码</label> <input type="password"
									class="form-control" id="password" name="password"
									placeholder="密码，长度8-16位">
							</div>
							<div class="form-group">
								<label for="password">密码</label>
								 <input type="password" class="form-control" id="confirmpsw" name="confirmpsw"
																		placeholder="请再次确认密码">
							</div>
							<div class="form-group">
								<label for="email">邮箱</label> <input type="email"
									class="form-control" id="email" name="email" placeholder="邮箱务必正确">
							</div>
							<div class="form-group" style="display: none">
								<label for="email">类型</label>
								<br>
								<span style="padding-right: 5px;">用户</span>
								<input type="radio" name="userType" value="1" checked="checked">
								<span style="padding-right: 5px;">管理员</span>
								<input type="radio" name="userType" value="2">
							</div>
							<button type="button" class="btn btn-warning" id="reg_btn">注册</button>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
  </div>
</div>
	<jsp:include page="components/foot.jsp"></jsp:include>
	<!-- jQuery (Bootstrap 的 JavaScript 插件需要引入 jQuery) -->

	<script src="/resources/js/jquery.js"></script>
	<!-- 引入 Bootstrap -->
	<script src="/resources/js/bootstrap.min.js"></script>
  <script src="/resources/js/register.js"></script>
</body>
</html>