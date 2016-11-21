package com.zxcc.httpClientTest;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * Created by xuanzh.cc on 2016/9/24.
 */
public class TestConnectionManagerPooling2 {
    public static void main(String[] args) throws InterruptedException {
        PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();
        //设置最大连接数
        httpClientConnectionManager.setMaxTotal(100);
        //设置每个路径上默认创建的最大连接
        httpClientConnectionManager.setDefaultMaxPerRoute(20);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(httpClientConnectionManager)
                .build();

        String[] URIS = new String[]{
            "http://xuanzh.cc/",
            "http://www.baidy.com/",
            "http://jelon.top/",
        };

        GetThread[] getThreads = new GetThread[URIS.length];

        for (int i = 0; i < URIS.length; i++) {
            HttpGet httpGet = new HttpGet(URIS[i]);
            getThreads[i] = new GetThread(httpClient, httpGet);
        }

        for (int i = 0; i < getThreads.length; i++) {
            getThreads[i].start();
        }
        for (int i = 0; i < getThreads.length; i++) {
            getThreads[i].join();
        }

        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 请求执行线程 */
    static class GetThread extends Thread{

        private final CloseableHttpClient httpClient;
        private final HttpContext httpContext;
        private final HttpGet httpGet;

        public GetThread(CloseableHttpClient httpClient, HttpGet httpGet) {
            this.httpGet = httpGet;
            this.httpContext = HttpClientContext.create();
            this.httpClient = httpClient;
        }

        @Override
        public void run() {
            CloseableHttpResponse response = null;

            try {
                response = httpClient.execute(httpGet, httpContext);

                System.out.println(response.getEntity().getContentType().getName());
                System.out.println(response.getEntity().getContentType().getValue());

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(response != null) {
                    try {
                        response.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}
