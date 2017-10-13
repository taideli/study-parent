package com.tdl.study.elasticsearch.xpack;

import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.xpack.watcher.actions.Action;

import java.io.IOException;

public class JdbcAction implements Action {

    @Nullable
    String schema;

    @Nullable
    String ip;

    @Nullable
    String port;

    @Nullable
    String namespace;

    @Nullable
    String table;

    @Nullable
    String username;

    @Nullable
    String password;

    @Nullable
    TimeValue timeout;

    @Override
    public String type() {
        return "jdbc";
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();

        return null;
    }


    public static class Builder implements org.elasticsearch.xpack.watcher.actions.Action.Builder<JdbcAction> {

        @Override
        public JdbcAction build() {
            return null;
        }
    }
}
