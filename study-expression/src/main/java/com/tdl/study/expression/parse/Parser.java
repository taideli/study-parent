package com.tdl.study.expression.parse;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class Parser {
    private String expression;

    public Parser(String expression) {
        this.expression = expression;
    }

    public List<Token> parse() throws IOException {
        StringReader reader = new StringReader(expression);
        reader.mark(0);
        reader.reset();
        int b = -1;
        while (-1 != (b = reader.read())) {
            Character.isWhitespace(b);
        }
        reader.close();
        return null;

        // 词法分析  语法分析
    }
}
