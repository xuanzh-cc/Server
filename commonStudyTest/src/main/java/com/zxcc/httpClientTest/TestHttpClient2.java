package com.zxcc.httpClientTest;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.message.BasicHttpResponse;

/**
 * Created by xuanzh.cc on 2016/9/21.
 */
public class TestHttpClient2 {
    public static void main(String[] args) {
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");

        response.addHeader("Set-Cookie", "k1=v1; path=/; domain=localhost");
        response.addHeader("Set-Cookie", "k2=v2; path=\"/\"; domain=\"localhost\"");

        Header headerFirst = response.getFirstHeader("Set-Cookie");
        System.out.println(headerFirst);

        Header headerLast = response.getLastHeader("Set-Cookie");
        System.out.println(headerLast);

        Header[] headers = response.getAllHeaders();
        System.out.println(headers.length);


        System.out.println(response.getProtocolVersion());
        System.out.println(response.getStatusLine().getStatusCode());
        System.out.println(response.getStatusLine().getReasonPhrase());
        System.out.println(response.getStatusLine().toString());

    }
}
