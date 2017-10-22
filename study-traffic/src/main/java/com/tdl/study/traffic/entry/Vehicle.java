package com.tdl.study.traffic.entry;

public class Vehicle {
    private String plateCode;
    private Color color;

    public static void main(String[] args) {
        System.out.println(Color.valueOf("RED").getCode());
    }

    public enum Color {
        WHITE("A") , // A 白
        GRAY("B"), // B 灰
        YELLOW("C"), // C 黄
        PINK("D"), // D 粉
        RED("E"), // E 红
        PURPLE("F"), // F 紫
        GREEN("G"), // G 绿
        BLUE("H"), // H 蓝
        BROWN("I"), // I 棕
        BLACK("J"), // J 黑
        OTHER("Z"); // Z 其它

        private String code;

        Color(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

    }
}
