package com.zxcc.blog.tag.entity;

import java.io.Serializable;

/**
 * 标签
 * Created by xuanzh.cc on 2016/8/14.
 */
public class Tag implements Serializable{

    private static final long serialVersionUID = 3994032530166797211L;

    /** 标签ID */
    private long tagId;
    /** 标签名称 */
    private String tagName;

    public static Tag valueOf(long tagId) {
        Tag tag = new Tag();
        tag.tagId = tagId;
        return tag;
    }

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
