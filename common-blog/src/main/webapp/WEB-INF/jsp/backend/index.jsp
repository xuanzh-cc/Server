<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/8/21
  Time: 2:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@include file="./include/segment_tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

    <title>xuanzh.cc</title>
    <meta name="keywords" content="xuanzh.cc">
    <meta name="description" content="个人博客，xuanzh.cc">
    <%@include file="./include/segment_css.jsp"%>

</head>
<body class="fixed-sidebar full-height-layout gray-bg" style="overflow:hidden">
<div id="wrapper">
    <%-- 左侧 - 菜单 --%>
    <%@include file="./include/segment_menu.jsp"%>
    <%-- 右侧 - 内容 --%>
    <div id="page-wrapper" class="gray-bg dashbard-1">
        <%-- 最顶端 --%>
        <div class="row border-bottom">
            <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
                <div class="navbar-header">
                    <a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"><i class="fa fa-bars"></i> </a>
                </div>
                <ul class="nav navbar-top-links navbar-right">
                    <li class="dropdown">
                        <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
                            <i class="fa fa-bell"></i> <span class="label label-primary">8</span>
                        </a>
                        <ul class="dropdown-menu dropdown-alerts">
                            <li>
                                <a href="mailbox.html">
                                    <div>
                                        <i class="fa fa-envelope fa-fw"></i> 您有16条未读消息
                                        <span class="pull-right text-muted small">4分钟前</span>
                                    </div>
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <a href="profile.html">
                                    <div>
                                        <i class="fa fa-qq fa-fw"></i> 3条新回复
                                        <span class="pull-right text-muted small">12分钟前</span>
                                    </div>
                                </a>
                            </li>
                            <li class="divider"></li>
                            <li>
                                <div class="text-center link-block">
                                    <a class="menuItem" href="notifications.html">
                                        <strong>查看所有 </strong>
                                        <i class="fa fa-angle-right"></i>
                                    </a>
                                </div>
                            </li>
                        </ul>
                    </li>
                </ul>
            </nav>
        </div>

        <%-- 主体内容部分 --%>
        <div class="row mainContent" id="content-main">
            <iframe class="J_iframe" name="iframe0" width="100%" height="100%" src="${ctx}/admin/main" frameborder="0" data-id="${ctx}/admin/main" seamless></iframe>
        </div>

        <%-- footer 部分 --%>
        <div class="footer">
            <div class="pull-right">&copy; 2016 <a href="http://www.xuanzh.cc/" target="_blank">xuanzh's blog</a>
            </div>
        </div>
    </div>
</div>

    <%-- 密码修改模态框 --%>
    <div id="password-modify-modal" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true" style="top:100px;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
                    <h4 class="modal-title">类别修改</h4>
                </div>
                <small class="font-bold">
                    <div class="modal-body">
                        <div class="form-group"><label>原密码</label> <input type="password" id="old-password" class="form-control"></div>
                        <div class="form-group"><label>新密码</label> <input type="password" id="new-password" class="form-control"></div>
                        <div class="form-group"><label>新密码确认</label> <input type="password" id="new-password-repeated" class="form-control"></div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
                        <button type="button" class="btn btn-primary" id="password-modify-confirm-btn">修改</button>
                    </div>
                </small>
            </div>
        </div>
    </div>

<%@include file="./include/segment_js.jsp"%>
<%--<script type="text/javascript" src="${ctx}/resources/js/contabs.min.js"></script>--%>

</body>
</html>