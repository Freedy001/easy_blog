package com.freedy.backend;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Date;

/**
 * @author Freedy
 * @date 2021/4/29 13:11
 */
@Slf4j
@Data
public class Text {

    public final int a=100;

    public static void main(String[] args) throws Exception {
        SpelExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext(new Text());
        context.setVariable("hello","hello");
        context.setVariable("word","word");
        Expression expression = parser.parseExpression("#hello+#word+a");
        String hello_word = expression.getValue(context,String.class);
        System.out.println(hello_word);
    }
}

