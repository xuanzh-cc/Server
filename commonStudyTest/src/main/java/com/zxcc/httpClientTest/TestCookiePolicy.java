package com.zxcc.httpClientTest;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Created by xuanzh.cc on 2016/10/13.
 */
public class TestCookiePolicy {

    public static void main(String[] args) {
        RequestConfig globalConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.DEFAULT)
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(globalConfig)
                .build();

        RequestConfig localConfig = RequestConfig.copy(globalConfig)
                .setCookieSpec(CookieSpecs.STANDARD_STRICT)
                .build();

        HttpGet httpGet = new HttpGet("http://xuanzh.cc");
        httpGet.setConfig(localConfig);
        //...
        //发送请求。。。
        //处理请求
        //...
    }
}
