package com.tdl.study.hadoop.weatcher;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


import java.io.IOException;

public class MaxTemperatureMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private static final int MISSING = 9999;

    /*@Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String year = line.substring(15, 19);
        int airTemperature = 0;
        try {
            if (line.charAt(88) == '+' || line.charAt(88) == ' ') {
                airTemperature = Integer.parseInt(line.substring(89, 93));
            } else {
                airTemperature = Integer.parseInt(line.substring(88, 93));
            }
        } catch (Exception e) {
            System.out.println("can not get temperature(" + line.substring(88, 92) + "   " + line.substring(87, 92) +  "--> " + line.charAt(87) + "), line: " + line);
        }
        String quality = line.substring(92, 93);
        if (airTemperature != MISSING && quality.matches("[01459]")) {
            context.write(new Text(year), new IntWritable(airTemperature));
        }
    }*/

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        Record record = Record.of(line);
        if (null == record) return;
        String year = record.YEARMODA.substring(0, 3);
        int airTemperature = 0;
        airTemperature = Integer.parseInt(record.MXSPD.substring(0, record.MXSPD.indexOf('.')));
        String quality = record.GUST;
        if (airTemperature != MISSING /*&& quality.matches("[01459]")*/) {
            context.write(new Text(year), new IntWritable(airTemperature));
        }
    }
}
