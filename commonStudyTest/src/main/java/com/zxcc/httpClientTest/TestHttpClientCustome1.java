package com.zxcc.httpClientTest;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuanzh.cc on 2016/9/21.
 */
public class TestHttpClientCustome1 {
    public static void main(String[] args) {
        ConnectionKeepAliveStrategy keepAliveStrategy = new DefaultConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                // 返回 Keep-Alive 消息头 的timeout的值 * 1000 ，没有该值得花则返回 -1
                // 例如 Keep-Alive:timeout=20
                long keepAlive = super.getKeepAliveDuration(response, context);
                if(keepAlive == -1) {
                    //服务器没有设置Keep-Alive 消息头，则返回-1
                }
                keepAlive = 5000;
                return keepAlive;
            }
        };
    }
}
