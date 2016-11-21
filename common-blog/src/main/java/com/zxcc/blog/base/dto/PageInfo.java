package com.zxcc.blog.base.dto;

/**
 * 分页信息
 * Created by xuanzh.cc on 2016/9/14.
 */
public class PageInfo {
    /** 页面上最多显示5个分页 */
    private static int SHOW_PAGE_NUM = 5;

    /**
     * 设置 页面上最多显示的分页数目
     * @param showPageNum
     */
    public static void setShowPageNum(int showPageNum){
        SHOW_PAGE_NUM = showPageNum;
    }

    /** 总页数 */
    private int totalPage;
    /** 当前页数 */
    private int currentPage;
    /** 开始条数 */
    private int offset;
    /** 每页要显示的数量 */
    private int limit;

    /** 页面显示开始页 */
    private int beginPage;
    /** 页面显示结束页 */
    private int endPage;

    /**
     * 实例化方法
     * @param totalNum
     * @param totalPage
     * @param currentPage
     * @param pageSize
     * @return
     */
    public static PageInfo valueOf(int totalNum, int totalPage, int currentPage, int pageSize) {
        currentPage = currentPage < 1 ? 1 : currentPage;
        currentPage = currentPage > totalPage ? totalPage : currentPage;
        pageSize = pageSize < 5 ? 5 : pageSize;
        PageInfo pageInfo = new PageInfo();
        pageInfo.totalPage = totalPage;
        pageInfo.currentPage = currentPage;
        pageInfo.offset = (currentPage - 1) * pageSize;
        pageInfo.offset = pageInfo.offset < 0 ? 0 : pageInfo.offset;
        pageInfo.limit = pageSize;

        int beginPage;
        int endPage;
        int separatedNum = SHOW_PAGE_NUM / 2;
        if(totalPage <= SHOW_PAGE_NUM) {
            beginPage = 1;
            endPage = totalPage;
        } else {
            beginPage = currentPage - separatedNum < 1 ? 1 : currentPage - separatedNum;
            endPage = beginPage + SHOW_PAGE_NUM - 1;
            if(endPage > totalPage ) {
                endPage = totalPage;
                beginPage = totalPage - SHOW_PAGE_NUM + 1 ;
            }
        }
        pageInfo.beginPage = beginPage;
        pageInfo.endPage = endPage;
        return pageInfo;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public int getBeginPage() {
        return beginPage;
    }

    public int getEndPage() {
        return endPage;
    }
}
