package com.tdl.study.hadoop.weather;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Record {
    public static final DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    final String STN;
    final String WBAN;
    final String YEARMODA;
    final String TEMP;
    final String TEMP_1;
    final String DEWP;
    final String DEWP_1;
    final String SLP;
    final String SLP_1;
    final String STP;
    final String STP_1;
    final String VISIB;
    final String VISIB_1;
    final String WDSP;
    final String WDSP_1;
    final String MXSPD;
    final String GUST;
    final String MAX;
    final String MIN;
    final String PRCP;
    final String SNDP;
    final String FRSHIT;

    public Record(String STN, String WBAN, String YEARMODA, String TEMP, String TEMP_1,
                  String DEWP, String DEWP_1, String SLP, String SLP_1, String STP,
                  String STP_1, String VISIB, String VISIB_1, String WDSP, String WDSP_1,
                  String MXSPD, String GUST, String MAX, String MIN, String PRCP,
                  String SNDP, String FRSHIT) {
        this.STN = STN;
        this.WBAN = WBAN;
        this.YEARMODA = YEARMODA;
        this.TEMP = TEMP;
        this.TEMP_1 = TEMP_1;
        this.DEWP = DEWP;
        this.DEWP_1 = DEWP_1;
        this.SLP = SLP;
        this.SLP_1 = SLP_1;
        this.STP = STP;
        this.STP_1 = STP_1;
        this.VISIB = VISIB;
        this.VISIB_1 = VISIB_1;
        this.WDSP = WDSP;
        this.WDSP_1 = WDSP_1;
        this.MXSPD = MXSPD;
        this.GUST = GUST;
        this.MAX = MAX;
        this.MIN = MIN;
        this.PRCP = PRCP;
        this.SNDP = SNDP;
        this.FRSHIT = FRSHIT;
    }

    public static Record of(String line) {
        if (null == line || line.isEmpty()) return null;
        String[] fields = line.split("\\s+");
        if (fields.length < 22) {
//            System.out.println("record field missing, total: " + fields.length + ", line: " + line);
            return null;
        }

        String STN = fields[0];
        String WBAN = fields[1];
        String YEARMODA = fields[2];
        String TEMP = fields[3];
        String TEMP_1 = fields[4];
        String DEWP = fields[5];
        String DEWP_1 = fields[6];
        String SLP = fields[7];
        String SLP_1 = fields[8];
        String STP = fields[9];
        String STP_1 = fields[10];
        String VISIB = fields[11];
        String VISIB_1 = fields[12];
        String WDSP = fields[13];
        String WDSP_1 = fields[14];
        String MXSPD = fields[15];
        String GUST = fields[16];
        String MAX = fields[17];
        String MIN = fields[18];
        String PRCP = fields[19];
        String SNDP = fields[20];
        String FRSHIT = fields[21];

        return new Record(STN, WBAN, YEARMODA, TEMP, TEMP_1, DEWP, DEWP_1,
                SLP, SLP_1, STP, STP_1, VISIB, VISIB_1, WDSP, WDSP_1,
                MXSPD, GUST, MAX, MIN, PRCP, SNDP, FRSHIT);
    }
}
