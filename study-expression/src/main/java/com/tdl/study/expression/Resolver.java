package com.tdl.study.expression;

import com.tdl.study.expression.parse.Parser;
import com.tdl.study.expression.parse.Token;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 表达式解析器
 */
public class Resolver {
    private String expression;
    List<Token> tokens = new LinkedList<>();

    public void compile(String expression) throws IOException {
        this.expression = expression;
        rpn(analyze());
    }

    /**
     * 把字符串转换为Tokens
     * @return
     */
    private List<Token> analyze() throws IOException {
        Parser parser = new Parser(expression);
        return parser.parse();
    }

    /**
     * 把tokens 转换为 逆波兰表达式
     * @param tokens
     */
    private void rpn(List<Token> tokens) {

    }

}
