import com.zxcc.big.data.utils.FSUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.s3.Block;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.io.IOUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by xuanzh.cc on 2016/9/27.
 */
public class TestHdfsApi {

    /**
     * 测试读取
     */
    @Test
    public void tesrRead(){
        FileSystem fileSystem = FSUtils.getFileSystem();

        //Path path = new Path("/wc/input/core-site.xml");
        Path path = new Path("/wc/input/file02.txt");

        try {
            FSDataInputStream fsDataInputStream = fileSystem.open(path);

            IOUtils.copyBytes(fsDataInputStream, System.out, 2048, false);

            IOUtils.closeStream(fsDataInputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试列出文件
     */
    @Test
    public void testList(){
        FileSystem fileSystem = FSUtils.getFileSystem();

        Path path = new Path("/wc/input");

        try {
            FileStatus[] fileStatuses = fileSystem.listStatus(path);
            for(FileStatus fileStatus : fileStatuses){
                String info = fileStatus.isDir() ? "目录" : "文件 ,  ";
                info += fileStatus.getPath().getName();

                System.out.println(info);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试创建目录
     */
    @Test
    public void testMkdir(){
        FileSystem fileSystem = FSUtils.getFileSystem();

        Path path = new Path("/wc/newDir/");

        try {
            boolean result = fileSystem.mkdirs(path);
            System.out.println(result ? "目录创建成功..." : "目录创建失败...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试上传文件
     */
    @Test
    public void testPut(){
        FileSystem fileSystem = FSUtils.getFileSystem();

        Path localPath = new Path("C:\\Users\\Administrator\\Desktop\\hadoop001-s010.vmdk");
        Path destPath = new Path("/wc/newDir/newFile.exe");

        try {
            fileSystem.copyFromLocalFile(localPath, destPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试创建文件
     */
    @Test
    public void testCreate(){
        FileSystem fileSystem = FSUtils.getFileSystem();

        Path path = new Path("/wc/input/file02.txt");

        try {
            FSDataOutputStream outputStream = fileSystem.create(path);

            outputStream.writeUTF("这是新创建的文件。。。");

            IOUtils.closeStream(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试重命名
     */
    @Test
    public void testRename(){
        FileSystem fileSystem = FSUtils.getFileSystem();

        Path oldPath = new Path("/wc/newDir/newFile.exe");
        Path newPath = new Path("/wc/newDir/hadoop001-s010.vmdk");
        try {
            boolean result = fileSystem.rename(oldPath, newPath);

            System.out.println(result ? "文件名称修改成功！" : "文件名称修改失败！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试删除文件 或者目录（指定参数为 true 的话在，则为递归删除）
     */
    @Test
    public void testDelete(){
        FileSystem fileSystem = FSUtils.getFileSystem();

        Path newPath = new Path("/wc/newDir/");

        try {
            fileSystem.delete(newPath, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查找文件快的位置和信息
     */
    @Test
    public void testFindBlock(){
        FileSystem fileSystem = FSUtils.getFileSystem();

        Path path = new Path("/wc/newDir/hadoop001-s010.vmdk");

        try {
            FileStatus fileStatus = fileSystem.getFileStatus(path);

            BlockLocation[] blockLocations = fileSystem.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());

            for (BlockLocation blockLocation : blockLocations) {
                String[] blockNames = blockLocation.getNames();
                String blockName = "[";
                for (String bn : blockNames) {
                    blockName += " " + bn + " ";
                }
                blockName += "]";
                String blockInfo = "块名称：" + blockName + ", 文件大小：" + blockLocation.getLength() + " kb";

                System.out.println(blockInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有的 dataNode 信息
     */
    @Test
    public void testGetAllDataNodes(){
        FileSystem fileSystem = FSUtils.getFileSystem();

        DistributedFileSystem distributedFileSystem = (DistributedFileSystem)fileSystem;

        try {
            DatanodeInfo[] datanodeInfos = distributedFileSystem.getDataNodeStats();
            for (DatanodeInfo datanodeInfo : datanodeInfos) {
                String dataNodeInfoStr =
                        "DatanodeReport: " + datanodeInfo.getDatanodeReport() + "\n" +
                        "HostName: " + datanodeInfo.getHostName()  + "\n" +
                        "Host: " + datanodeInfo.getHost()  + "\n" +
                        "Name" + datanodeInfo.getName()  + "\n" +
                        "NetworkLocation: " + datanodeInfo.getNetworkLocation() + "\n" +
                        "StorageID: " + datanodeInfo.getStorageID() + "\n" +
                        "RemainingPercent：" + datanodeInfo.getRemainingPercent() + "%\n" +
                       "Capacity：" + datanodeInfo.getCapacity() + "\n" +
                        "InfoPort：" + datanodeInfo.getInfoPort()  + "\n" +
                        "IpcPort：" + datanodeInfo.getIpcPort()  + "\n" +
                        "Level：" + datanodeInfo.getLevel();

                System.out.println(dataNodeInfoStr);
                System.out.println("----------------------------------\r\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件合并上传
     */
    @Test
    public void testPutMerge(){

        Path localPath = new Path("H:\\log\\log\\flow");

        Path destPath = new Path("/wc/newDir/wlgame_log_file.log");
        FileSystem fileSystem = FSUtils.getFileSystem();

        FSDataOutputStream outputStream = null;
        try {
            FileSystem localFileSystem = FileSystem.getLocal(new Configuration());
            outputStream = fileSystem.create(destPath);
            long now = System.currentTimeMillis();
            this.writeToHDFS(localFileSystem, localPath, outputStream);
            System.out.println("总共花费了" + (System.currentTimeMillis() - now) + "ms");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeStream(outputStream);
        }
    }

    /**
     * 写到 HDFS 文件系统
     * @param localPath
     * @param outputStream
     * @throws IOException
     */
    private void writeToHDFS(FileSystem localFileSystem, Path localPath, FSDataOutputStream outputStream) throws IOException {

        FileStatus fileStatus = localFileSystem.getFileStatus(localPath);

        if (fileStatus.isDir()) {
            System.out.println("目录：" + fileStatus.getPath().toString());
            FileStatus[] fileStatuses = localFileSystem.listStatus(localPath);
            for(FileStatus tmpFileStatus : fileStatuses){
                writeToHDFS(localFileSystem, tmpFileStatus.getPath(), outputStream);
            }
        } else {
            System.out.println("文件：" + fileStatus.getPath().toString());
            FSDataInputStream inputStream = localFileSystem.open(localPath);

            byte[] buff = new byte[4096];
            int len = 0;
            while((len = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0 , len);
            }
            IOUtils.closeStream(inputStream);
        }

    }
}
