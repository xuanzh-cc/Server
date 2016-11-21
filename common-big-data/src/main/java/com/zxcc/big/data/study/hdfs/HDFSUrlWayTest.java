package com.zxcc.big.data.study.hdfs;

import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by xuanzh.cc on 2016/9/26.
 */
public class HDFSUrlWayTest {

    private static final String url = "hdfs://hadoop-master.zxcc.com:9000/wc/input/core-site.xml" ;
    // 让程序识别 HDFS 的url
    static {
        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
    }

    public static void main(String[] args) {

        InputStream inputStream = null;

        try {
            inputStream = new URL(url).openStream();

            IOUtils.copyBytes(inputStream, System.out, 4096, false);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(inputStream);
        }

    }
}
