package com.tdl.study.kudu;

import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;
import org.apache.kudu.Type;
import org.apache.kudu.client.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class KuduMain {
    private KuduClient client;
    private KuduSession session;

    public KuduMain() {
        client = new KuduClient
                .KuduClientBuilder("172.30.12.26")
                .workerCount(5)
                .build();
//        client = ((AsyncKuduClient) client).syncClient();
        session = client.newSession();
        session.setFlushMode(SessionConfiguration.FlushMode.AUTO_FLUSH_BACKGROUND);
        session.setTimeoutMillis(2000);
//        session.apply() // CRUD
    }

    private boolean isExistTable(String table) throws KuduException {
        assert null != table;
        return client.tableExists(table);
    }

    public void createTable(String table) throws KuduException {

        if (isExistTable(table)) {
            System.out.println("table " + table + " exist");
            deleteTable(table);
        }
        List<String> scl = Arrays.asList("ID XM XB DZ CODE".split(" "));
        List<ColumnSchema> columns = scl.stream()
                .map(column -> {
                    ColumnSchema.ColumnSchemaBuilder csb = new ColumnSchema.ColumnSchemaBuilder(column, Type.STRING);
                    if (column.equals("ID")) {
                        csb.key(true);
                        csb.nullable(false);
                    } else {
                        csb.nullable(true);
                    }

                    return csb.build();
                }).collect(Collectors.toList());
        Schema schema = new Schema(columns);
        CreateTableOptions cto = new CreateTableOptions();
        List<String> pk = Arrays.asList("ID");
        cto.setRangePartitionColumns(pk);
        KuduTable kt = client.createTable(table, schema, cto);
        if (null == kt) {
            System.out.println("return kt is null");
            return;
        }
        System.out.println(kt.getName() + " id is " + kt.getTableId());
    }

    void deleteTable(String name) throws KuduException {
        DeleteTableResponse response = client.deleteTable(name);
        System.out.println("delete: " + response);
    }

    public void listTables() throws KuduException {
        ListTablesResponse response = client.getTablesList();
        List<String> tables = response.getTablesList();
        System.out.println("tables: " + tables);
    }

    void desc(String table) throws KuduException {
        KuduTable kt = client.openTable(table);
        Schema schema = kt.getSchema();
        System.out.println(table + " schema:  " + schema);
    }

    void writeData() throws KuduException {
        KuduTable table = client.openTable("CREATE_TEST");
        Insert insert = table.newInsert();
        insert.getRow().addString("ID", UUID.randomUUID().toString());
        insert.getRow().addString("XM", "taideli");
        insert.getRow().addString("XB", "男");
        insert.getRow().addString("DZ", "浙江省杭州市");
        insert.getRow().addString("CODE", "123456");
        session.apply(insert);
        session.flush();
    }

    void readData() throws KuduException {
        KuduTable table = client.openTable("CREATE_TEST");
        KuduScanner scanner = client.newScannerBuilder(table).build();
        while (scanner.hasMoreRows()) {
            scanner.nextRows().iterator().forEachRemaining(rs -> {
                System.out.println(rs.rowToString());
            });
        }
        scanner.close();
    }

    public void close() throws KuduException {
        if (null != client) client.close();
    }



    public static void main(String[] args) throws KuduException {

        KuduMain main = new KuduMain();
//        main.listTables();
        main.createTable("CREATE_TEST");
        main.listTables();
        main.desc("CREATE_TEST");
        main.writeData();
        main.readData();
        main.close();


    }

}
