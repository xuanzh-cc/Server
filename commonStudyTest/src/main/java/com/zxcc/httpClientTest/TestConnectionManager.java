package com.zxcc.httpClientTest;

import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuanzh.cc on 2016/9/24.
 */
public class TestConnectionManager {
    public static void main(String[] args) {
        HttpClientContext clientContext = HttpClientContext.create();
        HttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager();
        HttpRoute route = new HttpRoute(new HttpHost("xuanzh.cc", 80));

        //请求一个新的连接,这个过程可能需要一点时间
        ConnectionRequest connectionRequest = connectionManager.requestConnection(route, null);

        HttpClientConnection clientConnection = null;
        try {
            clientConnection = connectionRequest.get(10, TimeUnit.SECONDS);

            //连接还没有打开
            if (!clientConnection.isOpen()) {
                //根据 路径信息打开连接
                connectionManager.connect(clientConnection, route, 1000, clientContext);
                //把它标记为路径已经计算完成
                connectionManager.routeComplete(clientConnection, route, clientContext);
            }

            //这里使用该连接做需要要做的事情


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (ConnectionPoolTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         finally {
            if(clientConnection != null) {
                connectionManager.releaseConnection(clientConnection, null, 1, TimeUnit.SECONDS);
            }
        }
    }
}
