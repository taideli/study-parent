package com.tdl.study.solr.unique;

import java.sql.*;

public class SolrSqlMain {

    //  curl --data-urlencode 'stmt=select * from tdl_dev_core limit 1000' http://172.16.17.11:10180/solr/tdl_dev_core/sql?aggregationMode=facet
    public static void main(String[] args) throws SQLException {

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        String zkString = "172.16.17.11:12181";
//        String collection = "tdl_dev_core";
        String collection = "tdl_dev_test_2";
        String aggregationMode = "facet"; // facet or map_reduce
        String uri = "jdbc:solr://" + zkString + "?collection=" + collection + "&aggregationMode=" + aggregationMode + "&numWorks=2";
        con = DriverManager.getConnection(uri);
        String sql = "select * from tdl_dev_core t1 join tdl_dev_test_2 t2 on t1.id = t2.id limit 1000"; // select * 必须得带limit，否则会报错
//        String sql = "select * from tdl_dev_core";

        stmt = con.createStatement();
        rs = stmt.executeQuery(sql);

        ResultSetMetaData metaData = rs.getMetaData();
        int clonumCount = metaData.getColumnCount();
        String lineSpace = "\t";
        for (int i = 1; i <= clonumCount; i++) {
            System.out.print(metaData.getColumnName(i) + lineSpace);
        }
        System.out.println();
        while (rs.next()) {
            for (int i = 1; i <= clonumCount; i++) {
                System.out.print(rs.getString(metaData.getColumnName(i)) + lineSpace);
            }
            System.out.println();
        }
        rs.close();
        stmt.close();
        con.close();
        System.out.println("====end");

    }
}
