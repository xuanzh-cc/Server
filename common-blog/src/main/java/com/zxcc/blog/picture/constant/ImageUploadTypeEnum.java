package com.zxcc.blog.picture.constant;

/**
 * 图片上传类型
 * Created by xuanzh.cc on 2016/8/21.
 */
public enum ImageUploadTypeEnum {
    common(1, "通用类型"),
    articleImage(2, "文章上传"),
    userImage(3, "用户头像"),
    selfPhoto(4, "个人相册"),
    ;
    //类别ID
    private int id;
    //类别名称
    private String name;
    //类别 对应的上传目录
    private String imageDirPath;

    ImageUploadTypeEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageDirPath() {
        return imageDirPath;
    }

    public void setImageDirPath(String imageDirPath) {
        this.imageDirPath = imageDirPath;
    }

    /**
     * 根据id获取类型
     * @param uploadTypeId
     * @return
     */
    public static ImageUploadTypeEnum getById(int uploadTypeId) {
        for(ImageUploadTypeEnum imageUploadTypeEnum : ImageUploadTypeEnum.values()){
            if(imageUploadTypeEnum.getId() == uploadTypeId) {
                return imageUploadTypeEnum;
            }
        }
        return null;
    }
}
