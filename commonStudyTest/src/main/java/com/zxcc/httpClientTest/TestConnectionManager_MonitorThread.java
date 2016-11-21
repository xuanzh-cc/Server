package com.zxcc.httpClientTest;

import org.apache.http.conn.HttpClientConnectionManager;

import java.util.concurrent.TimeUnit;

/**
 * Created by xuanzh.cc on 2016/9/24.
 */
public class TestConnectionManager_MonitorThread extends Thread{

    private final HttpClientConnectionManager connectionManager;
    private volatile boolean shutdown;

    public TestConnectionManager_MonitorThread(HttpClientConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void run() {

        while (!shutdown) {
            synchronized (this) {
                try {
                    //每5秒检查一次
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //关闭过期的连接
                connectionManager.closeExpiredConnections();

                //关闭空闲时间超过30秒的连接
                connectionManager.closeIdleConnections(30, TimeUnit.SECONDS);

            }
        }
    }

    /**
     * 关闭监控线程
     */
    public void shutdown(){
        this.shutdown = true;
    }

}
