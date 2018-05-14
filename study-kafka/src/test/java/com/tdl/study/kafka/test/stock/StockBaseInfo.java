package com.tdl.study.kafka.test.stock;

public class StockBaseInfo {
    /*股票类型*/
    private String symbolType;
    /*股票代码*/
    private String symbol;
    /*价格精度*/
    private int pricePrecision;
    /*一手为多少股*/
    private int hand;
    /*当前价*/
    private double consecutivePresentPrice;
    /*当前价采集时间*/
    private long consecutivePresentPriceTimestamp;
    /*开盘价*/
    private double open;

}
