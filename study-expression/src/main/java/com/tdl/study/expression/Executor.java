package com.tdl.study.expression;

import com.tdl.study.expression.exception.InvalidExpressionException;

import java.io.IOException;

/**
 * 执行器，执行解析器解析后的结果
 */
public class Executor {

    public Executor(Context ctx, Resolver resolver) {
    }

    public Object execute() {
        return null;
    }

    public static Object execute(String expression) throws InvalidExpressionException, IOException {
        return execute(expression, new Context());
    }

    public static Object execute(String expression, Context ctx) throws InvalidExpressionException, IOException {
        return execute(expression, ctx, null);
    }

    public static Object execute(String expression, Context ctx, Evaluator evaluator) throws InvalidExpressionException, IOException {
        if (null == expression || expression.isEmpty()) throw new InvalidExpressionException();
        if (null == ctx) ctx = new Context();
        if (null != evaluator) ctx.setEvaluator(evaluator);

        Resolver resolver = new Resolver();
        resolver.compile(expression);

        Executor executor = new Executor(ctx, resolver);

        return executor.execute();
    }
}
