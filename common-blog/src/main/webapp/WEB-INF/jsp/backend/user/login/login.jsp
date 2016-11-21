<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../../include/segment_tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

    <title>登录</title>
    <meta name="keywords" content="xuanzh.cc">
    <meta name="description" content="个人博客，xuanzh.cc">
    <%@include file="../../include/segment_css.jsp"%>
    <link href="${ctx}/resources/css/login.min.css" rel="stylesheet">

    <script>if(window.top !== window.self){ window.top.location = window.location;}</script>
</head>

<body class="signin">
<div class="signinpanel">
    <div class="row">
        <div class="col-sm-offset-3 col-sm-6">
            <form method="post">
                <input type="text" name="username" class="form-control uname" id="username" placeholder="用户名" />
                <input type="password" name="password" class="form-control pword m-b" id="password" placeholder="密码" />
                <button class="btn btn-success btn-block" type="button" id="admin-login-btn">登录</button>
            </form>
        </div>
    </div>
</div>

<%@include file="../../include/segment_js.jsp"%>

</body>

</html>

