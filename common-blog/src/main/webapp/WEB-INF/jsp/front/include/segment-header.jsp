<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>

    <%@include file="segment_browsehappy.jsp"%>

    <header class="header">
        <div class="container">
            <!-- logo -->
            <div class="nav-logo">
                <a href="${ctx}/"><img src="${ctx}/resources/images/user/xuanzh.png"></a>
                <span class="nav-logo-smaller">xuanzh 的窝</span>
            </div>
            <!-- 导航 -->
            <nav class="nav-collapse hidden">
                <ul>
                    <li><a href="${ctx}/"><i class="fa fa-home"></i> 首页</a></li>
                    <%--<li><a href="${ctx}/article/">文章</a></li>--%>
                    <!-- 搜索 -->
                    <li>
                        <div class="nav-search">
                            <form action="/blog/s" method="post" id="nav-search-form">
                                <input type="text" class="nav-search-input" name="keyword" placeholder="good luck">
                                <button class="btn nav-search-btn" type="submit"><i class="fa fa-search"></i></button>
                            </form>

                        </div>
                    </li>
                </ul>
            </nav>

            <a href="javascript:;" class="nav-toggle"></a>
        </div>
    </header>