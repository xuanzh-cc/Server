package com.zxcc.httpClientTest;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

/**
 * Created by xuanzh.cc on 2016/9/21.
 */
public class TestHttpRequestRetryHandler {
    public static void main(String[] args) {
        HttpRequestRetryHandler handler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                //重试超过三次，则放弃
                if(executionCount >= 3) {
                    System.out.println("准备重试，当前次数为：" +  executionCount + "  超过最大次数....");
                    return false;
                }

                //超时
                if(exception instanceof InterruptedIOException) {
                    return false;
                }

                //未知的地址
                if( exception instanceof UnknownHostException) {
                    System.out.println("准备重试，当前次数为：" +  executionCount);
                    return true;
                }

                //链接被拒绝
                if(exception instanceof ConnectTimeoutException) {
                    return false;
                }

                //SSL 握手异常
                if(exception instanceof SSLHandshakeException) {
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();

                //幂等性
                boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                if(idempotent) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        CloseableHttpClient client = HttpClients.custom()
                .setRetryHandler(handler)
                .build();

        HttpGet httpGet = new HttpGet("http://xuanzh1.cc");

        CloseableHttpResponse response = null;
        try {
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
