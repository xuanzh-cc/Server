package com.zxcc.httpClientTest;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by xuanzh.cc on 2016/9/22.
 */
public class TestEntity1 {
    public static void main(String[] args) throws IOException {
        StringEntity entity = new StringEntity("这是一个消息", ContentType.create("text/plain", "UTF-8"));

        //获取Content-Type 消息头
        System.out.println(entity.getContentType());
        //获取 Content-Length 消息头
        System.out.println(entity.getContentLength());
        //使用entity设置的字符编码，返回内容字符串
        System.out.println(EntityUtils.toString(entity));
        //返回内容的字节数组
        System.out.println(EntityUtils.toByteArray(entity));
    }
}
