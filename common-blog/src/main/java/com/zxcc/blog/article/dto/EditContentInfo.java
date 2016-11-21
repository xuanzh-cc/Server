package com.zxcc.blog.article.dto;

/**
 * 文章编辑页面 dto
 * Created by xuanzh.cc on 2016/8/21.
 */
public class EditContentInfo {
    //是否是新建文章
    private boolean isNewCreate;
    //文章信息
    private ArticleDtoDetail atricleDto;


    public EditContentInfo(boolean isNewCreate) {
        this.isNewCreate = isNewCreate;
    }

    public EditContentInfo(boolean isNewCreate, ArticleDtoDetail atricleDto) {
        this.isNewCreate = isNewCreate;
        this.atricleDto = atricleDto;
    }

    public boolean getIsNewCreate() {
        return isNewCreate;
    }

    public void setIsNewCreate(boolean newCreate) {
        isNewCreate = newCreate;
    }

    public ArticleDtoDetail getAtricleDto() {
        return atricleDto;
    }

    public void setAtricleDto(ArticleDtoDetail atricleDto) {
        this.atricleDto = atricleDto;
    }
}
