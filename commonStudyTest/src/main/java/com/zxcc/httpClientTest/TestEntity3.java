package com.zxcc.httpClientTest;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuanzh.cc on 2016/9/22.
 */
public class TestEntity3 {
    public static void main(String[] args) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", "username_ha"));
        params.add(new BasicNameValuePair("password", "my_pwd"));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, Consts.UTF_8);
        HttpPost formPost = new HttpPost("http://mysite/login");
        formPost.setEntity(formEntity);
        CloseableHttpResponse loginResponse = client.execute(formPost);

        File file = new File("C:\\Users\\Administrator\\Desktop\\a.png");
        FileEntity fileEntity = new FileEntity(file, ContentType.create("image/png", "UTF-8"));
        HttpPost httpPost = new HttpPost("http://xuanzh.cc/blog/picture/admin/upload");
        CloseableHttpResponse response = client.execute(httpPost);

        if (response != null) {
            System.out.println(EntityUtils.toString(response.getEntity()));
        }

    }
}
