<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>

                    <div class="page-navigator">
                        <c:if test="${pageInfo != null}">
                            <c:if test="${pageInfo.currentPage > 1}">
                                <a class="btn btn-page" href="${ctx}/article/page/${pageInfo.currentPage - 1}">上一页</a>
                            </c:if>
                            <c:if test="${pageInfo.beginPage > 1}">
                                <a class="btn btn-page" href="${ctx}/article/page/1">1 ... </a>
                            </c:if>

                            <c:forEach begin="${pageInfo.beginPage}" end="${pageInfo.endPage}" var="i">
                                <c:if test="${pageInfo.currentPage == i}">
                                    <a class="btn btn-page btn-page-active"><span>${i}</span></a>
                                </c:if>
                                <c:if test="${pageInfo.currentPage != i}">
                                    <a class="btn btn-page" href="${ctx}/article/page/${i}">${i}</a>
                                </c:if>
                            </c:forEach>

                            <c:if test="${pageInfo.endPage < pageInfo.totalPage}">
                                <a class="btn btn-page" href="${ctx}/article/page/${pageInfo.totalPage}"> ... ${pageInfo.totalPage}</a>
                            </c:if>

                            <c:if test="${pageInfo.currentPage < pageInfo.totalPage}">
                                <a class="btn btn-page" href="${ctx}/article/page/${pageInfo.currentPage + 1}">下一页</a>
                            </c:if>
                        </c:if>
                    </div>