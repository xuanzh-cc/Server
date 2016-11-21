<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>

<!--左侧导航开始-->
<nav class="navbar-default navbar-static-side" role="navigation">
    <div class="nav-close"><i class="fa fa-times-circle"></i></div>
    <div class="sidebar-collapse">
        <ul class="nav" id="side-menu">
            <%-- 个人信息区域 --%>
            <li class="nav-header">
                <div class="dropdown profile-element">
                    <span><img alt="image" class="img-circle" src="${ctx}/resources/images/user/${user.imageUrl}" /></span>
                    <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                        <span class="clear">
                            <span class="block m-t-xs"><strong class="font-bold">${user.nickname}</strong></span>
                            <span class="text-muted text-xs block">${user.sign}<b class="caret"></b></span>
                        </span>
                    </a>
                    <ul class="dropdown-menu animated fadeInRight m-t-xs">
                        <li><a class="menuItem" href="profile">个人资料</a></li>
                        <li><a id="password-modify">密码修改</a></li>
                        <li class="divider"></li>
                        <li><a href="${ctx}/user/admin/logout">安全退出</a></li>
                    </ul>
                </div>
                <div class="logo-element">xuanzh</div>
            </li>

            <%-- 主页 --%>
            <li>
                <a class="menuItem" href="${ctx}/admin/main"><i class="fa fa-home"></i><span class="nav-label">首页</span></a>
            </li>

            <%-- 文章 --%>
            <li>
                <a href="#"><i class="fa fa fa-bar-chart-o"></i><span class="nav-label">文章</span><span class="fa arrow"></span></a>
                <ul class="nav nav-second-level">
                    <li><a class="menuItem" href="${ctx}/article/admin/editorPage">新建文章</a></li>
                    <li><a class="menuItem" href="${ctx}/article/admin/list">文章列表</a></li>
                </ul>
            </li>

            <%-- 类别 --%>
            <li>
                <a class="menuItem" href="${ctx}/category/admin/list"><i class="fa fa-gift"></i><span class="nav-label">类别管理</span></a>
            </li>

            <%-- 标签 --%>
            <li>
                <a class="menuItem" href="${ctx}/tag/admin/list"><i class="fa fa-tags"></i><span class="nav-label">标签管理</span></a>
            </li>

        </ul>
    </div>
</nav>
<!--左侧导航结束-->