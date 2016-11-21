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

    <title>文章编辑</title>
    <meta name="keywords" content="xuanzh.cc">
    <meta name="description" content="个人博客，xuanzh.cc">
    <%@include file="../include/segment_css.jsp"%>

    <link rel="stylesheet" type="text/css" href="${ctx}/resources/css/editor/simditor.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/css/editor/third-emoji/simditor-emoji.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/css/editor/third-html/simditor-html.css" media="screen" charset="utf-8"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/css/editor/third-markdown/simditor-markdown.css" media="screen" charset="utf-8"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/resources/css/editor/prism/prism.css"/>

</head>
<body class="gray-bg">
    <div class="wrapper wrapper-content">
        <%-- 右侧 - 内容 --%>
        <div class="row">
            <div class="col-sm-12">
                <div class="simditor">
                    <form id="form-article-editor" class="form-group">
                        <input type="text" class="form-control input-lg input-article-title" id="article-title"placeholder="文章标题">
                        <select class="form-control" name="categoryId" id="article-categoryId">
                            <c:forEach var="category" items="${categories}">
                            <option value="${category.categoryId}">${category.categoryName}</option>
                            </c:forEach>
                        </select>
                        <textarea id="editor" data-autosave="editor-content" autofocus data-autosave-confirm>
                        </textarea>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <%@include file="../include/segment_js.jsp"%>

    <script type="text/javascript" src="${ctx}/resources/js/editor/module.min.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/editor/hotkeys.min.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/editor/uploader.min.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/editor/simditor.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/editor/third-emoji/simditor-emoji.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/editor/third-autosave/simditor-autosave.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/editor/third-mark/simditor-mark.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/beautify/beautify-html.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/editor/third-html/simditor-html.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/prism/prism.js"></script>

    <script type="text/javascript" src="${ctx}/resources/js/dtd.js"></script>
    <script type="text/javascript" src="${ctx}/resources/js/browser.js"></script>

    <c:if test="${editContentInfo.isNewCreate == false}">
        <div id="simditor-tmp-container">${editContentInfo.atricleDto.content}</div>
    </c:if>

    <script type="text/javascript">
        var editContentInfo = {
            <c:if test="${editContentInfo.isNewCreate == true}">
            isNewCreate : true,
            </c:if>
            <c:if test="${editContentInfo.isNewCreate == false}">
            isNewCreate : false,
            atricleDto : {
                articleId : '${editContentInfo.atricleDto.articleId}',
                category : {
                    categoryId : '${editContentInfo.atricleDto.category.categoryId}',
                    categoryName : '${editContentInfo.atricleDto.category.categoryName}',
                },
                title: '${editContentInfo.atricleDto.title}',
                content: $("#simditor-tmp-container").html(),
                tags: [
                    <c:forEach var="tag" items="${editContentInfo.atricleDto.tags}">
                    {
                        tagId: '${tag.tagId}',
                        tagName: '${tag.tagName}'
                    },
                    </c:forEach>
                ],
            },
            </c:if>
        };

        <c:if test="${editContentInfo.isNewCreate == false}">
        $("#simditor-tmp-container").remove();
        </c:if>
        initSimditor(editContentInfo);

        /* 心跳, 防止session在编辑文章的时候过期 */
        setInterval(function(){
            doAjax({}, 'get', '/blog/article/admin/heartbeat')
            .then(function (data) {
                if(data.success){
                    console.log("heartbeat success...");
                }
            }, function () {
                console.log("heartbeat fail...");
            });
        }, 30000);

    </script>
</body>
</html>
