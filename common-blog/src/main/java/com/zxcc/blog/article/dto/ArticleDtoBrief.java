package com.zxcc.blog.article.dto;

import com.zxcc.blog.tag.entity.Tag;

import java.util.List;

/**
 * 文章简短dto
 * Created by xuanzh.cc on 2016/8/14.
 */
public class ArticleDtoBrief extends ArticleDtoBase{

    private String summary;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
