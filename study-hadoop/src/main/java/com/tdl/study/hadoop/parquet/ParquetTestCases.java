package com.tdl.study.hadoop.parquet;

import com.tdl.study.hadoop.TestCases;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.column.ParquetProperties;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.GroupFactory;
import org.apache.parquet.example.data.simple.SimpleGroupFactory;
import org.apache.parquet.hadoop.ParquetFileWriter;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.example.GroupReadSupport;
import org.apache.parquet.hadoop.example.GroupWriteSupport;
import org.apache.parquet.io.api.Binary;
import org.apache.parquet.schema.GroupType;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.MessageTypeParser;

import java.io.IOException;

public class ParquetTestCases extends TestCases {
    private Configuration conf;

    public ParquetTestCases() {
        super();
        conf = new Configuration();
    }

    public void simpleWrite() throws IOException {
        MessageType schema = MessageTypeParser.parseMessageType(
                "message Pair " +
                      "{ " +
                        "required binary left (UTF8); " +
                        "required binary right (UTF8);" +
                      "}");
        GroupFactory factory = new SimpleGroupFactory(schema);
//        GroupType.ID
        Group group = factory.newGroup()
                .append("left", "L")
                .append("right", "R");

        GroupWriteSupport writeSupport = new GroupWriteSupport();
        GroupWriteSupport.setSchema(schema, conf);
        Path path = new Path("/user/root/parquet/test01.parquet");
//        ParquetFileWriter
        ParquetWriter<Group> writer = new ParquetWriter<>(path, writeSupport,
                ParquetWriter.DEFAULT_COMPRESSION_CODEC_NAME,
                ParquetWriter.DEFAULT_BLOCK_SIZE,
                ParquetWriter.DEFAULT_PAGE_SIZE,
                ParquetWriter.DEFAULT_PAGE_SIZE,
                ParquetWriter.DEFAULT_IS_DICTIONARY_ENABLED,
                ParquetWriter.DEFAULT_IS_VALIDATING_ENABLED,
                ParquetProperties.WriterVersion.PARQUET_2_0, conf);
        writer.write(group);
        writer.close();
    }

    public void simpleRead() throws IOException {
        Path path = new Path("/user/root/parquet/test01.parquet");
        GroupReadSupport readSupport = new GroupReadSupport();
        ParquetReader<Group> reader = ParquetReader.builder(readSupport, path).build();
        Group group = null;
        while (null != (group = reader.read())) {
            /*group.getType().getFields().forEach(type -> {
                type.getName()
            });*/
            String l = group.getString("left", 0);
            String r = group.getString("right", 0);
            System.out.printf("value: [l, r] [%s, %s]\n", l, r);
            int columnCount = group.getType().getFieldCount();

        }
        reader.close();
        System.out.println("\nread finished");
    }
}
