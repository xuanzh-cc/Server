package com.zxcc.httpClientTest;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by xuanzh.cc on 2016/9/21.
 */
public class TestHttpClient1 {
    public static void main(String[] args) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = null;
        try {
            URI uri = new URIBuilder()
                    .setScheme("http")
                    .setHost("xuanzh.cc")
                    //.setPath("/s")
                    //.setParameter("keyword", "spring")
                    .build();

            httpGet = new HttpGet(uri);

            System.out.println(httpGet.getURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }



        CloseableHttpResponse httpResponse = null;

        try {
            httpResponse = httpClient.execute(httpGet);
            //对返回做处理
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "utf-8"));
            String str = null;
            while( (str = reader.readLine()) != null){
                System.out.println(str);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
