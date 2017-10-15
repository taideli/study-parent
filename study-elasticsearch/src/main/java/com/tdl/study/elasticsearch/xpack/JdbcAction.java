package com.tdl.study.elasticsearch.xpack;

import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.xpack.watcher.actions.Action;

import java.io.IOException;
import java.net.URI;

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

    public JdbcAction(String schema, String ip, String port, String namespace, String table,
                      String username, String password, TimeValue timeout) {
        this.schema = schema;
        this.ip = ip;
        this.port = port;
        this.namespace = namespace;
        this.table = table;
        this.username = username;
        this.password = password;
        this.timeout = timeout;
    }

    @Override
    public String type() {
        return "jdbc";
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        builder.field(Field.SCHEMA.getPreferredName(), this.schema);
        builder.field(Field.IP_ADDRESS.getPreferredName(), this.ip);
        builder.field(Field.PORT.getPreferredName(), this.port);
        builder.field(Field.NAMESPACE.getPreferredName(), this.namespace);
        builder.field(Field.TABLE.getPreferredName(), this.table);
        builder.field(Field.USERNAME.getPreferredName(), this.username);
        builder.field(Field.PASSWORD.getPreferredName(), this.password);
        if (null != this.timeout) {
            builder.timeValueField(Field.TIMEOUT.getPreferredName(), Field.TIMEOUT_HUMAN.getPreferredName(), this.timeout);
        }
        builder.endObject();
        return builder;
    }

    interface Field extends org.elasticsearch.xpack.watcher.actions.Action.Field {
        ParseField SCHEMA = new ParseField("schema", new String[0]);
        ParseField IP_ADDRESS = new ParseField("ip_address", new String[0]);
        ParseField PORT = new ParseField("port", new String[0]);
        ParseField NAMESPACE = new ParseField("namespace", new String[0]);
        ParseField TABLE = new ParseField("table", new String[0]);
        ParseField USERNAME = new ParseField("username", new String[0]);
        ParseField PASSWORD = new ParseField("password", new String[0]);
        ParseField TIMEOUT = new ParseField("timeout_in_millis", new String[0]);
        ParseField TIMEOUT_HUMAN = new ParseField("timeout", new String[0]);
    }

    public static class Builder implements org.elasticsearch.xpack.watcher.actions.Action.Builder<JdbcAction> {

        private String uri;

        public Builder(String uri) {
            this.uri = uri;
        }

        @Override
        public JdbcAction build() {
            return new JdbcAction("jdbc:mysql", "192.168.1.104", "3306",
                    "default", "WATCHER_RESULT", "username", "password",
                    TimeValue.timeValueSeconds(30));
        }
    }
}
