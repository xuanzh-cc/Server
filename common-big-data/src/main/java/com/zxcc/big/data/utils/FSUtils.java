package com.zxcc.big.data.utils;

import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;

import java.io.IOException;
import java.net.URI;

/**
 * Created by xuanzh.cc on 2016/9/26.
 */
public class FSUtils {


    /**
     * 获取 hdfs 文件系统
     * @return
     */
    public static FileSystem getFileSystem(){
        // 获取配置文件信息
        Configuration configuration = new Configuration();

        //获取文件系统
        FileSystem fileSystem = null;
        try {
            fileSystem = FileSystem.get(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileSystem;

    }

}
