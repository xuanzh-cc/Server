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
            <div class="wrapper wrapper-content  animated fadeInRight blog">
                <div class="row">
                    <div class="col-sm-offset-1 col-sm-10">
                        <div class="ibox">
                            <div class="ibox-title">
                                <h5>所有类别</h5>
                                <div class="ibox-tools">
                                    <a href="projects.html" class="btn btn-primary btn-xs">创建新项目</a>
                                </div>
                            </div>
                            <div class="ibox-content">
                                <div class="row m-b-sm m-t-sm">
                                    <div class="col-md-offset-11 col-md-1">
                                        <button type="button" id="loading-example-btn" class="btn btn-white btn-sm"><i class="fa fa-refresh"></i> 刷新</button>
                                    </div>
                                </div>
                                <div class="category-list">
                                    <table class="table table-hover">
                                        <tbody>
                                            <tr>
                                                <td class="project-title">
                                                    <a href="project_detail.html">LIKE－一款能够让用户快速获得认同感的兴趣社交应用</a>
                                                    <br/>
                                                    <small>创建于 2014.08.15</small>
                                                </td>
                                                <td class="project-title">
                                                    <a href="project_detail.html">LIKE－一款能够让用户快速获得认同感的兴趣社交应用</a>
                                                    <br/>
                                                    <small>40</small> 篇
                                                </td>
                                                <td class="project-actions">
                                                    <a href="projects.html#" class="btn btn-white btn-sm"><i class="fa fa-folder"></i> 查看 </a>
                                                    <a href="projects.html#" class="btn btn-white btn-sm"><i class="fa fa-pencil"></i> 编辑 </a>
                                                </td>
                                            </tr>
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
    
    <%@include file="../include/segment_js.jsp"%>
    <script type="text/javascript" src="${ctx}/resources/js/prism/prism.js"></script>
</body>
</html>
