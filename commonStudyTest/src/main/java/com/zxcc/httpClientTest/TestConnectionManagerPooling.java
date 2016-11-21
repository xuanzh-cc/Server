package com.zxcc.httpClientTest;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;

/**
 * Created by xuanzh.cc on 2016/9/24.
 */
public class TestConnectionManagerPooling {
    public static void main(String[] args) {
        PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();
        //设置最大连接数
        httpClientConnectionManager.setMaxTotal(200);
        //设置每个路径上默认创建的最大连接
        httpClientConnectionManager.setDefaultMaxPerRoute(20);

        HttpHost httpHost = new HttpHost("xuanzh.cc", 80);
        //将该 host 上创建的最大连接数设为50
        httpClientConnectionManager.setMaxPerRoute(new HttpRoute(httpHost), 50);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(httpClientConnectionManager)
                .build();

        HttpGet httpGet = new HttpGet("http://xuanzh.cc/blog");

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            System.out.println(response.getEntity().getContentType().getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
         finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
