package com.zxcc.httpClientTest;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xuanzh.cc on 2016/9/21.
 */
public class TestHttpInterceptor {
    public static void main(String[] args) {
        final AtomicInteger atomicInteger = new AtomicInteger(1);

        CloseableHttpClient client = HttpClients.custom()
                .addInterceptorLast(new HttpRequestInterceptor() {
                    @Override
                    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
                        atomicInteger.getAndIncrement();
                        System.out.println("当前的 count：" + atomicInteger.get());
                    }
                })
                .build();

        HttpGet httpGet = new HttpGet("http://xuanzh.cc");

        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpGet);
            response = client.execute(httpGet);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
