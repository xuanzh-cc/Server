package com.zxcc.big.data.study.hdfs;

import com.zxcc.big.data.utils.FSUtils;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;

/**
 * Created by xuanzh.cc on 2016/9/27.
 */
public class HDFSTestCreate {

    public static void main(String[] args) {
        FileSystem fileSystem = FSUtils.getFileSystem();

        Path path = new Path("/wc/input/file01.txt");

        try {
            FSDataOutputStream outputStream = fileSystem.create(path);
            outputStream.writeUTF("你好， 哈哈！");

            IOUtils.closeStream(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
