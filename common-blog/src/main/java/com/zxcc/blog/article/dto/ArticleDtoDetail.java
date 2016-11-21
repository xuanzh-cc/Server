package com.zxcc.blog.article.dto;

import java.util.List;
import com.zxcc.blog.tag.entity.Tag;

/**
 * 文章详情dto
 * Created by xuanzh.cc on 2016/8/21.
 */
public class ArticleDtoDetail extends ArticleDtoBase{

    //内容
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
