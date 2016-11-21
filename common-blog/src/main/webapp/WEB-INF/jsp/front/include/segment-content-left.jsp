<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>

            <%-- 个人简短信息--%>
            <%--<div class="profile"></div>--%>
            <%-- 类别 --%>
            <div class="category list">
                <h3><b>类别</b></h3>
                <span class="span-mark"></span>
                <ul>
                    <c:forEach var="category" items="${categories}">
                        <li>
                            <a href="${ctx}/category/${category.categoryId}/detail"><p>${category.categoryName}</p></a>
                            <span class="list-num pull-right"><i>${category.totalArticleNum}</i> 篇</span>
                        </li>
                    </c:forEach>
                </ul>
            </div>
            <%-- 文章归档 --%>
            <%--<div class="archive list">
                <h3><b>归档</b></h3>
                <span class="span-mark"></span>
                <ul>
                    <li><a href=""><p>2016年5月</p></a> <span class="list-num pull-right"><i class="">45</i></span></li>
                    <li><a href=""><p>2016年4月</p></a> <span class="list-num pull-right"><i class="">45</i></span></li>
                </ul>
            </div>--%>
            <%-- 友链 --%>
            <div class="friend-link list">
                <h3><b>友链</b></h3>
                <span class="span-mark"></span>
                <ul>
                    <li><a href="http://jelon.top" target="_blank"><p><i class="fa fa-heart"></i> jelon</p> </a></li>
                    <li><a href="http://blog.sina.com.cn/u/1825875765" target="_blank"><p><i class="fa fa-heart"></i> haoren</p> </a></li>
                </ul>
            </div>