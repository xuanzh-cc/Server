package com.zxcc.httpClientTest;

import org.apache.http.*;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.message.BasicHttpResponse;

/**
 * Created by xuanzh.cc on 2016/9/21.
 */
public class TestHttpClient4 {
    public static void main(String[] args) {
        HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, HttpStatus.SC_OK, "OK");

        response.addHeader("Set-Cookie", "k1=v1; path=/; domain=localhost");
        response.addHeader("Set-Cookie", "k2=v2; path=\"/\"; domain=\"localhost\"");

        HeaderElementIterator iter = new BasicHeaderElementIterator(response.headerIterator());

        while(iter.hasNext()) {
            HeaderElement element = iter.nextElement();
            System.out.println(element.getName() + " = " + element.getValue());

            NameValuePair[] pairs = element.getParameters();
            for (NameValuePair pair : pairs) {
                System.out.println("  " + pair);
            }
        }

    }
}
