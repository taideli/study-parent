package com.tdl.study.calcite.sql;

import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.avatica.util.Quoting;
import org.apache.calcite.runtime.Hook;
import org.apache.calcite.sql.*;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.impl.SqlParserImpl;
import org.apache.calcite.sql.util.SqlBasicVisitor;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.tools.Planner;

public class Main {

    public static void main(String[] args) throws SqlParseException {

        String sql = "SELECT group_concat(l.zjhm) AS zzjhm,\n" +
                "       concat(l.lgdm,l.rzday) AS flag\n" +
                "FROM\n" +
                "  (SELECT w.ZJHM,\n" +
                "          w.LGDM,\n" +
                "          substr(w.rzsj,1,8) AS rzday,\n" +
                "          max(w.zklsh) AS zklsh\n" +
                "   FROM edw_gazhk_lgy_nb w\n" +
                "   WHERE substr(w.RZSJ,1,4)='2017'\n" +
                "     AND substr(w.ZJHM,17,1) IN ('0',\n" +
                "                                 '2',\n" +
                "                                 '4',\n" +
                "                                 '6',\n" +
                "                                 '8')\n" +
                "   GROUP BY w.zjhm,\n" +
                "            w.lgdm,\n" +
                "            rzday) l\n" +
                "INNER JOIN edw_gazhk_newzzrk_jb_tab z ON l.ZJHM=z.sfzh\n" +
                "INNER JOIN\n" +
                "  (SELECT m.zjhm AS zjhm\n" +
                "   FROM\n" +
                "     (SELECT w.ZJHM,\n" +
                "             w.LGDM,\n" +
                "             substr(w.rzsj,1,8) AS rzday,\n" +
                "             max(w.zklsh) AS zklsh\n" +
                "      FROM edw_gazhk_lgy_nb w\n" +
                "      WHERE substr(w.RZSJ,1,4)='2017'\n" +
                "        AND substr(w.ZJHM,17,1) IN ('0',\n" +
                "                                    '2',\n" +
                "                                    '4',\n" +
                "                                    '6',\n" +
                "                                    '8')\n" +
                "      GROUP BY w.zjhm,\n" +
                "               w.lgdm,\n" +
                "               rzday) m\n" +
                "   INNER JOIN edw_gazhk_newzzrk_jb_tab z ON m.ZJHM=z.sfzh\n" +
                "   WHERE substr(m.rzday,1,4)='2017'\n" +
                "     AND substr(m.ZJHM,17,1) IN ('0',\n" +
                "                                 '2',\n" +
                "                                 '4',\n" +
                "                                 '6',\n" +
                "                                 '8')\n" +
                "     AND cast(from_unixtime(unix_timestamp(now()),'yyyy') AS int)- cast(substr(m.zjhm,7,4) AS int) BETWEEN 16 AND 40\n" +
                "     AND cast(substr(z.DJRQ,1,4) AS int) >2016\n" +
                "   GROUP BY m.zjhm\n" +
                "   HAVING count(m.zklsh) >2) lll ON l.zjhm=lll.zjhm\n" +
                "WHERE substr(l.rzday,1,4) IN ('2017')\n" +
                "  AND substr(l.ZJHM,17,1) IN ('0',\n" +
                "                              '2',\n" +
                "                              '4',\n" +
                "                              '6',\n" +
                "                              '8')\n" +
                "  AND cast(from_unixtime(unix_timestamp(now()),'yyyy') AS int)- cast(substr(l.zjhm,7,4) AS int) BETWEEN 16 AND 40\n" +
                "  AND cast(substr(z.DJRQ,1,4) AS int) >2016\n" +
                "GROUP BY concat(l.lgdm,l.rzday)\n" +
                "HAVING length(zzjhm) > 38";


//        sql = "select name, age, address from people where age > 30 limit 2,10";
//        sql = "select p.name AS n, p.age As a, p.address from people p where age > 30 order by age, name desc limit 10, 999";
//        sql = "SELECT id, name, CAST(created_at AS DATE) FROM redshift.users";
//         sql = "SELECT id, name, CAST(created_at AS DATE) FROM ( select * from xxx )";

        final SqlParser.ConfigBuilder parserConfig = SqlParser.configBuilder()
                .setParserFactory(SqlParserImpl.FACTORY)
                .setQuoting(Quoting.DOUBLE_QUOTE)
                .setUnquotedCasing(Casing.TO_UPPER)
                .setQuotedCasing(Casing.UNCHANGED)
//                .setConformance(SqlConformanceEnum.DEFAULT)
                .setConformance(SqlConformanceEnum.LENIENT) // allow everything
                .setCaseSensitive(true)
                ;
        SqlParser parser = SqlParser.create(sql, parserConfig.build());
//        SqlNode stmt = parser.parseStmt();
        SqlNode query = parser.parseQuery();

        SqlBasicVisitor<String> visitor = new SqlBasicVisitor<String>() {
            @Override
            public String visit(SqlLiteral literal) {
//                System.out.println("literal===>" + literal.toString());
                return super.visit(literal);
            }

            @Override
            public String visit(SqlCall call) {
//                System.out.println("call===>" + call.getClass().getName()  + "<-->" + call.getKind().name() + " <--> " + call.getKind().sql);
//                System.out.println(call.getFunctionQuantifier().getTypeName().getName());
                switch (call.getKind()) {
                    case SELECT:
                        SqlSelect select = (SqlSelect) call;
                        System.out.println("from   " + select.getFrom());
                        System.out.println("group  " + select.getGroup());
                        System.out.println("having " + select.getHaving());
                        System.out.println("offset " + select.getOffset());
                        System.out.println("order  " + select.getOrderList());
                        break;
                    case AS:
                    case AND:
                    case CAST:
                    default:
                        System.out.println(call.getKind().name() + " ???? " + call.getClass().getName());

                }
//                    call.getOperandList().forEach(n -> System.out.println(n.toString()));
                return super.visit(call);
            }

            @Override
            public String visit(SqlNodeList nodeList) {
                nodeList.forEach(n -> System.out.println("nodeList===>" + n.getClass().getName()));
                nodeList.forEach(node -> {
                    System.out.println("nodeList-->" + node.toString());
                });
                return super.visit(nodeList);
            }

            @Override
            public String visit(SqlIdentifier id) {
//                System.out.println("identifier===>" + id.getSimple());
                return super.visit(id);
            }

            @Override
            public String visit(SqlDataTypeSpec type) {

                return super.visit(type);
            }

            @Override
            public String visit(SqlDynamicParam param) {
                System.out.println("param index at " + param.getIndex());
                return super.visit(param);
            }

            @Override
            public String visit(SqlIntervalQualifier intervalQualifier) {
                return super.visit(intervalQualifier);
            }
        };

//        System.out.println("\n\n");
//        stmt.accept(visitor);
//        System.out.println("\n\n");
        query.accept(visitor);
//        System.out.println("\n\n");
//        expression.accept(visitor);
//        System.out.println("\n\n");



//        String actual = query.toSqlString(null, true).getSql();
//        System.out.println("--------------" + actual + "--------------");
//            Meta.StatementType statementType = parser.get


        Hook.PARSE_TREE.run(new Object[]{sql, query});
    }
}
