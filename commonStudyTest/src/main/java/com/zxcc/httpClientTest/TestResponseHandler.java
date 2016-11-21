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
public class TestResponseHandler {
    public static void main(String[] args) {
        CloseableHttpClient client = HttpClients.createDefault();

        ResponseHandler<String> handler = new ResponseHandler<String>() {
            @Override
            public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
                return null;
            }
        };

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", "username_ha"));
        params.add(new BasicNameValuePair("password", "my_pwd"));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, Consts.UTF_8);
        HttpPost formPost = new HttpPost("http://mysite/login");
        formPost.setEntity(formEntity);

        try {
            String result = client.execute(formPost, handler);

            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
