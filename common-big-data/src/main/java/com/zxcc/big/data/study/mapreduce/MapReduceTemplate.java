package com.zxcc.big.data.study.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * MapReduce 模板
 * Created by xuanzh.cc on 2016/9/27.
 */
public class MapReduceTemplate {

    /**
     * mapper 代码
     * LongWritable     Text           Text        IntWritable
     * 输入key类型    输入value类型     输出key类型     输出value类型
     */
    static class MyMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            super.map(key, value, context);
        }
    }

    /**
     * reducer 代码
     *    LongWritable               Text               Text         IntWritable
     *      输入key类型            输入value类型         输出key类型     输出value类型
     * 对应mapper的输出key  对应mapper的输出value
     */
    static class MyReduce extends Reducer<Text, IntWritable, Text, IntWritable>{
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            super.reduce(key, values, context);
        }
    }

    public static void main(String[] args){
        try {
            Configuration configuration = new Configuration();
            Job job = new Job(configuration, "myJib");

            // 1设置 job 运行的类
            job.setJarByClass(MapReduceTemplate.class);

            //2、设置 Mapper 和 Reducer
            job.setMapperClass(MyMapper.class);
            job.setReducerClass(MyReduce.class);

            //3、设置输入文件和输出文件 的目录
            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));

            //4、设置输出结果的 key 和 value 的类型
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);

            //5、提交Job， 等待运行结果，并在客户端显示运行信息
            boolean success = job.waitForCompletion(true);

            //结束程序
            System.exit(success ? 0 : 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
