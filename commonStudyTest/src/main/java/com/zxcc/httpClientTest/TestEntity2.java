package com.zxcc.httpClientTest;

import com.sun.jndi.toolkit.url.Uri;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by xuanzh.cc on 2016/9/22.
 */
public class TestEntity2 {
    public static void main(String[] args) throws IOException, URISyntaxException {
        CloseableHttpClient client = HttpClients.createDefault();

        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost("xuanzh.cc")
                .setPath("/s")
                .setParameter("keyword", "spring")
                .build();

        HttpGet httpGet = new HttpGet(uri);

        CloseableHttpResponse response = client.execute(httpGet);
        try{
            HttpEntity entity = response.getEntity();

            if(entity != null) {
                long contentLength = entity.getContentLength();
                //当content的字节数未知或者超过了Long.MAX_VALUE 的时候，就回返回 -1
                if(contentLength != -1) {
                    //一次性的将 entity 的内容读取到 字符串中，如果内容很长，
                    // 则会消耗比较多的内容，所以当内容很长的时候，不建议使用该方法
                    String content = EntityUtils.toString(entity);
                    System.out.println("使用EntityUtils读取");
                    System.out.println(content);
                }
                //内容太长了， 则分多读取
                else {
                    System.out.println("使用多次读取");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "utf-8"));
                    String str = null;
                    while( (str = reader.readLine()) != null){
                        System.out.println(str);
                    }
                }
            }
        } finally {
            response.close();
        }
    }
}
