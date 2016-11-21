package com.zxcc.httpClientTest;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * Created by xuanzh.cc on 2016/9/21.
 */
public class TestHttpContext {
    public static void main(String[] args) {
        HttpContext context = HttpClientContext.create();

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://xuanzh.cc");

        CloseableHttpResponse response = null;
        try {
            response = client.execute(httpGet, context);

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
