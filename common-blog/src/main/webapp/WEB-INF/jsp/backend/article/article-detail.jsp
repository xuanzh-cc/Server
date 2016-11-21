<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/8/21
  Time: 2:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@include file="../include/segment_tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

    <title>文章详情</title>
    <meta name="keywords" content="xuanzh.cc">
    <meta name="description" content="个人博客，xuanzh.cc">
    <%@include file="../include/segment_css.jsp"%>

    <link rel="stylesheet" type="text/css" href="${ctx}/resources/css/editor/prism/prism.css"/>

</head>
<body class="gray-bg">
    <div class="wrapper wrapper-content">
        <%-- 右侧 - 内容 --%>
        <div class="row">
            <div class="wrapper wrapper-content  animated fadeInRight article">
                <div class="row">
                    <div class="col-lg-10 col-lg-offset-1">
                        <div class="ibox">
                            <div class="ibox-title">
                                <button class="btn btn-w-m btn-default" type="button" id="back-to-last-url-btn">返回</button>
                                <div class="pull-right">
                                    <a href="${ctx}/article/admin/editorPage?articleId=${article.articleId}" class="btn btn-success btn-outline">编辑</a>
                                    <button class="btn btn-w-m btn-danger" type="button" id="article-delete-btn" data-article-id="${article.articleId}">删除</button>
                                </div>
                            </div>
                            <div class="ibox-content">
                                <div class="pull-right">
                                    <c:forEach var="tag" items="${article.tags}">
                                        <button class="btn btn-white btn-xs" type="button">${tag.tagName}</button>
                                    </c:forEach>
                                </div>
                                <div class="text-center article-title">
                                    <h1>
                                        ${article.title}
                                    </h1>
                                </div>
                                <div class="article-content">
                                    ${article.content}
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <%@include file="../include/segment_js.jsp"%>
    <script type="text/javascript" src="${ctx}/resources/js/prism/prism.js"></script>
</body>
</html>
