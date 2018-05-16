package com.tdl.study.hadoop.weather;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

public class MaxTemperature {
    public static void main(String[] args) throws Exception {
        if (2 != args.length) {
            System.out.println("Usage: MaxTemperature <input path> <output path>");
            System.exit(-1);
        }

        Configuration conf = new Configuration();
        // 启用map任务输出结果压缩 snappy
//        conf.setClass(Job.MAP_OUTPUT_COMPRESS_CODEC, SnappyCodec.class, CompressionCodec.class);

        Job job = Job.getInstance(conf, "max-temperature");
        job.setJarByClass(MaxTemperature.class);
        job.setMapperClass(MaxTemperatureMapper.class);
//        job.setCombinerClass(MaxTemperatureReducer.class);
        job.setReducerClass(MaxTemperatureReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        // 使用压缩
//        FileOutputFormat.setCompressOutput(job, true);
//        FileOutputFormat.setOutputCompressorClass(job, SnappyCodec.class);

        // 对一行数据还是一块数据压缩，推荐一块数据压缩，这样效果好一些
        SequenceFileOutputFormat.setOutputCompressionType(job, SequenceFile.CompressionType.BLOCK);


        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
