<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@include file="../include/segment_tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <%-- 使用ie9渲染 --%>
    <meta http-equiv="X-UA-Compatible" content="IE=9" >

    <title>xuanzh.cc - 关于我</title>
    <meta name="keywords" content="xuanzh.cc">
    <meta name="description" content="个人博客，xuanzh.cc">
    <%@include file="../include/segment-css.jsp"%>

</head>
<body class="main">

<%-- header --%>
<%@include file="../include/segment-header.jsp"%>

<div class="container">
    <div class="content">
        <%-- 页面左边部分 --%>
        <div class="content-left pull-left">
            <%@include file="../include/segment-content-left.jsp"%>
        </div>
        <%-- 页面右边部分 --%>
        <div class="content-right pull-right">
            <div class="article-holder list">
                <c:forEach var="article" items="${articles}">
                    <article class="article">
                        <div class="article-title">
                            <h1><a href="">这里放文章标题1</a></h1>
                            <div class="article-meta">
                                <span class="article-post-time">发表与 <fmt:formatDate value="${article.createTime}" pattern="yyyy-MM-dd"/></span>
                                <span class="article-post-category">分类：<a href="">${article.category.categoryName}</a></span>
                                <span class="article-post-read">浏览：${article.clickHit}</span>
                            </div>
                        </div>
                        <div class="article-content">
                                ${article.summary}
                        </div>
                        <div class="article-footer">
                            <a href="" >阅读全文</a>
                        </div>
                    </article>
                </c:forEach>

                <%-- 分页信息 --%>
                <%@include file="../include/segment-page-info.jsp"%>
            </div>
        </div>
    </div>
</div>

<!-- 页面底部  -->
<%@include file="../include/segment-footer.jsp"%>
</body>
</html>
