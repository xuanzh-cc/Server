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

    <title>文章列表</title>
    <meta name="keywords" content="xuanzh.cc">
    <meta name="description" content="个人博客，xuanzh.cc">
    <%@include file="../include/segment_css.jsp"%>

    <link rel="stylesheet" type="text/css" href="${ctx}/resources/css/editor/prism/prism.css"/>

</head>
<body class="gray-bg">
    <div class="wrapper wrapper-content">
        <%-- 右侧 - 内容 --%>
        <div class="row">
            <div class="wrapper wrapper-content animated fadeInRight blog">
                <c:if test="${articles.size() > 0}">
                <c:forEach var="article" items="${articles}">
                <div class="col-lg-offset-1 col-lg-10 my-article">
                    <div class="ibox-content">
                        <a href="${ctx}/article/admin/${article.articleId}/detail" class="btn-link">
                            <h2>
                                ${article.title}
                            </h2>
                        </a>
                        <div class="small m-b-xs">
                            <strong>${article.user.nickname}</strong> <span class="text-muted"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${article.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/> </span>
                        </div>
                        ${article.summary} />
                        <div class="row">
                            <div class="col-md-6">
                                <h5>标签：</h5>
                                <c:forEach var="tag" items="${article.tags}">
                                    <button class="btn btn-white btn-xs" type="button">${tag.tagName}</button>
                                </c:forEach>
                            </div>
                            <div class="col-md-6">
                                <div class="small text-right">
                                    <h5>状态：</h5>
                                    <div> <i class="fa fa-comments-o"> </i> 56 评论 </div>
                                    <i class="fa fa-eye"> </i> ${article.clickHit} 浏览
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                </c:forEach>
                </c:if>

                <c:if test="${articles.size() <= 0}">
                <div class="col-lg-offset-1 col-lg-10 my-article">
                    <div class="ibox-content">
                        <h2 class="text-center">暂无文章，快去写一篇吧！</h2>
                    </div>
                </div>
                </c:if>

                <%-- 分页 --%>
                <div class="col-lg-offset-1 col-lg-10">
                    <div class="btn-group">
                        <c:if test="${pageInfo.currentPage > 1}">
                            <a class="btn btn-white" href="${ctx}/article/admin/list?page=${pageInfo.currentPage - 1}">Previous</a>
                        </c:if>
                        <c:if test="${pageInfo.beginPage > 1}">
                            <a class="btn btn-white" href="${ctx}/article/admin/list?page=1">1 ... </a>
                        </c:if>


                        <c:forEach begin="${pageInfo.beginPage}" end="${pageInfo.endPage}" var="i">
                            <c:if test="${pageInfo.currentPage == i}">
                                <a class="btn btn-white active disabled"><span>${i}</span></a>
                            </c:if>
                            <c:if test="${pageInfo.currentPage != i}">
                                <a class="btn btn-white" href="${ctx}/article/admin/list?page=${i}">${i}</a>
                            </c:if>
                        </c:forEach>

                        <c:if test="${pageInfo.endPage < pageInfo.totalPage}">
                            <a class="btn btn-white" href="${ctx}/article/admin/list?page=${pageInfo.totalPage}"> ... ${pageInfo.totalPage}</a>
                        </c:if>

                        <c:if test="${pageInfo.currentPage < pageInfo.totalPage}">
                            <a class="btn btn-white" href="${ctx}/article/admin/list?page=${pageInfo.currentPage + 1}">Next</a>
                        </c:if>
                    </div>
                </div>

            </div>
        </div>
    </div>
    
    <%@include file="../include/segment_js.jsp"%>
    <script type="text/javascript" src="${ctx}/resources/js/prism/prism.js"></script>
</body>
</html>
