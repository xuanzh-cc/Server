package com.zxcc.big.data.study.hdfs;

import com.zxcc.big.data.utils.FSUtils;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by xuanzh.cc on 2016/9/26.
 */
public class HDFSDirectoryWayTest {
    public static void main(String[] args) {

        //获取文件系统
        FileSystem fileSystem = FSUtils.getFileSystem();

        //文件的路径
        Path path = new Path("/wc/input/core-site.xml");

        try {
            //打开输入流
            FSDataInputStream inputStream = fileSystem.open(path);

            //读取文件内容到控制台显示
            IOUtils.copyBytes(inputStream, System.out, 4096, false);

            // 关闭输入流
            IOUtils.closeStream(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
