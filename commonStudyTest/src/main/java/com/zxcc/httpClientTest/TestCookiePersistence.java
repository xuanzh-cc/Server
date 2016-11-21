package com.zxcc.httpClientTest;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuanzh.cc on 2016/10/13.
 */
public class TestCookiePersistence {

    public static void main(String[] args) {
        //创建一个存储 cookie 的本地对象
        CookieStore cookieStore = new BasicCookieStore();
        //创建一个 cookie
        BasicClientCookie cookie = new BasicClientCookie("name", "zxwcc");
        cookie.setDomain(".xuanzh.cc");
        cookie.setPath("/");
        //添加对存储对象
        cookieStore.addCookie(cookie);

        // 设置 存储对象
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCookieStore(cookieStore)
                .build();

        //登陆
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", "zx_stone@163.com"));
        params.add(new BasicNameValuePair("password", "Zx899615"));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, Consts.UTF_8);
        HttpPost formPost = new HttpPost("http://xuanzh.cc/blog/user/admin/login");
        formPost.setEntity(formEntity);
        try {
            CloseableHttpResponse loginResponse = httpClient.execute(formPost);

            //打印登陆结果，此时 cookie 信息已经存在 cookieStore中了
            if (loginResponse != null) {
                System.out.println(EntityUtils.toString(loginResponse.getEntity()));
                List<Cookie> cookieList = cookieStore.getCookies();
                for (Cookie c : cookieList) {
                    System.out.println(c.getName() + " : " + c.getValue());
                }
            }

            //此时上传文件的时候，发送的post请求会带上已有的 cookie，一起发送到服务器
            File file = new File("C:\\Users\\Administrator\\Desktop\\a.png");
            HttpPost httpPost = new HttpPost("http://xuanzh.cc/blog/picture/admin/upload");

            //构建带有文件的表单实体
            HttpEntity imageFormEntity = MultipartEntityBuilder.create()
                    .addBinaryBody("upload_file", file)
                    .addTextBody("uploadType", "2")
                    .setCharset(Charset.forName("UTF-8"))
                    .build();

            httpPost.setEntity(imageFormEntity);
            CloseableHttpResponse response = httpClient.execute(httpPost);

            if (response != null) {
                System.out.println(EntityUtils.toString(response.getEntity()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
