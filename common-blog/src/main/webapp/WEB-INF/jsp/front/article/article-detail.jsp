<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@include file="../include/segment_tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=9" >

    <title>${article.title}</title>
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
                <c:if test = "${article != null}">
                    <article class="article">
                        <div class="article-title">
                            <h1><a href="${ctx}/article/${article.articleId}/detail">${article.title}</a></h1>
                            <div class="article-meta">
                                <span class="article-post-time">发表与 <fmt:formatDate value="${article.createTime}" pattern="yyyy-MM-dd"/></span>
                                <span class="article-post-category">分类：<a href="${ctx}/category/${article.category.categoryId}/detail">${article.category.categoryName}</a></span>
                                <span class="article-post-read">浏览：${article.clickHit}</span>
                            </div>
                        </div>
                        <div class="article-content">
                                ${article.content}
                        </div>
                    </article>
                </c:if>
            </div>
        </div>
    </div>
</div>

<!-- 页面底部  -->
<%@include file="../include/segment-footer.jsp"%>
</body>
</html>
