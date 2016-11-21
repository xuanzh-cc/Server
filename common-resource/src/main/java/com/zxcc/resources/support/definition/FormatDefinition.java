package com.zxcc.resources.support.definition;

import com.zxcc.resources.constant.ResourcesType;
import org.apache.commons.lang.StringUtils;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * 格式信息定义
 * Created by xuanzh.cc on 2016/6/28.
 */
public class FormatDefinition {
    //资源路径
    private String location;
    //资源类型
    private ResourcesType resourcesType;
    //资源后缀
    private String suffix;

    @PostConstruct
    public void init(){
        if(StringUtils.endsWith(location, File.separator)) {
            location = StringUtils.substringAfterLast(location, File.pathSeparator);
        }
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ResourcesType getResourcesType() {
        return resourcesType;
    }

    public void setResourcesType(String resourcesType) {
        this.resourcesType = ResourcesType.valueOf(resourcesType);
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
