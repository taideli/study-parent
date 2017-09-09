package com.tdl.study.hadoop.hdfs.io.output;

import com.tdl.study.core.io.output.OutputImpl;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.GroupFactory;
import org.apache.parquet.example.data.simple.SimpleGroupFactory;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.example.GroupWriteSupport;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.MessageTypeParser;

import java.io.IOException;
import java.util.UUID;

public class ParquetOutput extends OutputImpl<Group> {
    private final GroupFactory factory;
    private ParquetWriter<Group> writer = null;

    public ParquetOutput(String schema, String dest) throws IOException {
        this(MessageTypeParser.parseMessageType(schema), dest, UUID.randomUUID().toString() + ".parquet");
    }

    public ParquetOutput(MessageType schema, String dest) throws IOException {
        this(schema, dest, UUID.randomUUID().toString() + ".parquet");
    }

    public ParquetOutput(String schema, String dest, String filename) throws IOException {
        this(MessageTypeParser.parseMessageType(schema), dest, filename);
    }

    public ParquetOutput(MessageType schema, String dest, String filename) throws IOException {
        factory = new SimpleGroupFactory(schema);
        String pathString = dest.replaceAll("/$", "") + "/" + filename.replaceAll("^/", "");
        writer = new ParquetWriter<>(new Path(pathString), new GroupWriteSupport(),
                ParquetWriter.DEFAULT_COMPRESSION_CODEC_NAME,
                ParquetWriter.DEFAULT_BLOCK_SIZE,
                ParquetWriter.DEFAULT_PAGE_SIZE,
                ParquetWriter.DEFAULT_PAGE_SIZE,
                ParquetWriter.DEFAULT_IS_DICTIONARY_ENABLED,
                ParquetWriter.DEFAULT_IS_VALIDATING_ENABLED,
                ParquetWriter.DEFAULT_WRITER_VERSION);
        open();
    }

    public GroupFactory groupFactory() {
        return factory;
    }

    @Override
    protected boolean enqueue(Group item) {
        try {
            writer.write(item);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public void close() {
        try {
            writer.close();
        } catch (IOException ignored) {} finally {
            super.close();
        }
    }
}
