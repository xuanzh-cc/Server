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

    <title>类别列表</title>
    <meta name="keywords" content="xuanzh.cc">
    <meta name="description" content="个人博客，xuanzh.cc">
    <%@include file="../include/segment_css.jsp"%>

</head>
<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInUp">
        <%-- 右侧 - 内容 --%>
        <div class="row">
            <div class="wrapper wrapper-content  animated blog">
                <div class="row">
                    <div class="col-sm-offset-1 col-sm-10">
                        <div class="ibox">
                            <div class="ibox-title">
                                <h5>所有类别</h5>
                                <div class="ibox-tools">
                                    <button type="button" id="category-create-modal-btn" class="btn btn-primary btn-xs" >+ 创建新类别</button>
                                </div>
                            </div>
                            <div class="ibox-content">
                                <div class="category-list">
                                    <table class="table table-hover">
                                        <tbody>
                                            <c:forEach var="category" items="${categories}">
                                            <tr>
                                                <td>
                                                    <p class="text-center"><strong>${category.categoryName}</strong></p>
                                                </td>
                                                <td>
                                                    <p class="text-center">创建于 <fmt:formatDate value="${category.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
                                                </td>
                                                <td>
                                                    <p class="text-center">优先级： <b>${category.priority}</b> </p>
                                                </td>
                                                <td>
                                                    <p class="text-center">文章数： <b>${category.totalArticleNum}</b> </p>
                                                </td>
                                                <td class="category-actions">
                                                    <button class="btn btn-sm btn-success btn-outline category-edit-btn" type="button"
                                                            data-category-id="${category.categoryId}"
                                                            data-category-name="${category.categoryName}"
                                                            data-category-priority="${category.priority}">编辑</button>
                                                    <button class="btn btn-sm btn-danger category-delete-btn" type="button" data-category-id="${category.categoryId}">删除</button>
                                                </td>
                                            </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <%-- 添加类别模态框 --%>
    <div id="category-create-modal" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true" style="top:100px;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
                    <h4 class="modal-title">类别添加</h4>
                </div>
                <small class="font-bold">
                    <div class="modal-body">
                        <div class="form-group"><label>类别名称</label> <input type="text" id="category-create-modal-input-name" class="form-control"></div>
                        <div class="form-group"><label>显示优先级</label> <input type="text" id="category-create-modal-input-priority" class="form-control"></div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
                        <button type="button" class="btn btn-primary" id="category-create-confirm-btn">添加</button>
                    </div>
                </small>
            </div>
        </div>
    </div>

    <%-- 类别修改模态框 --%>
    <div id="category-edit-modal" class="modal fade" tabindex="-1" role="dialog" aria-hidden="true" style="top:100px;">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span></button>
                    <h4 class="modal-title">类别修改</h4>
                </div>
                <small class="font-bold">
                    <div class="modal-body">
                        <div class="form-group"><label>类别名称</label> <input type="text" id="category-edit-modal-input-name" class="form-control"></div>
                        <div class="form-group"><label>显示优先级</label> <input type="text" id="category-edit-modal-input-priority" class="form-control"></div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
                        <button type="button" class="btn btn-primary" id="category-edit-confirm-btn">修改</button>
                    </div>
                </small>
            </div>
        </div>
    </div>

    <%@include file="../include/segment_js.jsp"%>
    <script type="text/javascript" src="${ctx}/resources/js/prism/prism.js"></script>
</body>
</html>
