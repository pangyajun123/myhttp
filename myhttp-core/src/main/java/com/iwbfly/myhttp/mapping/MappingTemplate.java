package com.iwbfly.myhttp.mapping;

import com.iwbfly.myhttp.config.VariableScope;
import com.iwbfly.myhttp.converter.json.JsonConverter;
import com.iwbfly.myhttp.exceptions.MyhttpRuntimeException;
import com.iwbfly.myhttp.reflection.MyhttpMethod;
import com.iwbfly.myhttp.utils.StringUtils;

import java.util.*;

public class MappingTemplate {
    private  String template;
    private List<MappingExpr> exprList;
    private VariableScope variableScope;

    int readIndex = -1;

    private boolean isEnd() {
        return readIndex >= template.length() - 1;
    }

    private char nextChar() {
        readIndex++;
        return template.charAt(readIndex);
    }

    private void skipSpaces() {
        if (isEnd()) {
            return;
        }
        char ch = watch(1);
        while ((ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r') && !isEnd()) {
            nextChar();
            ch = watch(1);
        }
    }

    private void match(char except) {
        if (isEnd()) {
            throw new MyhttpRuntimeException("Template Expression Parse Error:\n Not found '" + except + "', column " + readIndex + " at \"" + template + "\"");
        }
        char real = nextChar();
        if (except != real) {
            throw new MyhttpRuntimeException("Template Expression Parse Error:\n It except '" + except + "', But found '" + real + "', column " + readIndex + " at \"" + template + "\"");
        }
    }

    private void matchToken(MappingExpr expr, Token token) {
        if (expr.token != token) {
            throw new MyhttpRuntimeException("Template Expression Parse Error:\n It except " + token.getName() + ", But found " + expr.token.getName() + ", column " + readIndex + " at \"" + template + "\"");
        }
    }

    private char watch(int i) {
        if (template.length() <= readIndex + i) {
            syntaxErrorWatchN(template.charAt(template.length() - 1), i);
        }
        return template.charAt(readIndex + i);
    }

    public MappingTemplate(String template, VariableScope variableScope) {
        this.template = template;
        this.variableScope = variableScope;
        compile();
    }

    public void compile() {
        readIndex = -1;
        exprList = new ArrayList<MappingExpr>();
        StringBuffer buffer = new StringBuffer();

        while (!isEnd()) {
            char ch = nextChar();
            if (ch == '$') {
                char ch1 = watch(1);
                if (ch1 == '{') {
                    nextChar();
                    if (buffer.length() > 0) {
                        MappingString str = new MappingString(buffer.toString());
                        exprList.add(str);
                    }

                    buffer = new StringBuffer();
                    MappingExpr expr = parseExpression();
                    match('}');
                    if (expr != null) {
                        exprList.add(expr);
                    }
                    continue;
                }
            }
            else if (ch == '\\') {
//                char ch2 = nextChar();
//                buffer.append(ch);
                if (watch(1) == '$') {
                    ch = nextChar();
                    buffer.append(ch);
                } else {
                    buffer.append(ch);
                }
            }
            buffer.append(ch);
        }


        if (buffer.length() > 0) {
            MappingString str = new MappingString(buffer.toString());
            exprList.add(str);
        }
    }


    private void syntaxErrorWatch1(char ch) {
        throw new MyhttpRuntimeException("Template Expression Parse Error:\n Character '" + ch +
                "', column " + (readIndex + 2) + " at \"" + template + "\"");
    }


    private void syntaxErrorWatchN(char ch, int n) {
        throw new MyhttpRuntimeException("Template Expression Parse Error:\n Character '" + ch +
                "', column " + (readIndex + n + 1) + " at \"" + template + "\"");
    }


    public MappingExpr parseExpression() {
        MappingExpr expr = null;
        while (!isEnd()) {
            char ch = watch(1);
            switch (ch) {
                case ' ':
                case '\t':
                case '\r':
                case '\n':
                    skipSpaces();
                    break;
                case ')':
                case '}':
                    if (expr == null) {
                        syntaxErrorWatch1(ch);
                    }
                    if (expr instanceof MappingInteger) {
                        return new MappingIndex(((MappingInteger) expr).getNumber());
                    }
                    if (expr instanceof MappingIdentity) {
                        return new MappingReference(variableScope, ((MappingIdentity) expr).getName());
                    }
                    return expr;
                case '\'':
                    nextChar();
                    expr = parseString(ch);
                    match('\'');
                    break;
                case '\"':
                    nextChar();
                    expr = parseString(ch);
                    match('\"');
                    break;
                case '$':
                    nextChar();
                    expr = parseIndex();
                    break;
                case '.':
                    nextChar();
                    if (expr instanceof MappingIdentity) {
                        expr = new MappingReference(variableScope, ((MappingIdentity) expr).getName());
                    }
                    MappingIdentity id = parseIdentity();
                    expr = new MappingDot(variableScope, expr, id);
                    break;
                case '(':
                    nextChar();
                    MappingIdentity methodName = null;
                    if (expr == null) {
                        syntaxErrorWatch1(ch);
                    }
                    if (expr.token == Token.DOT && ((MappingDot) expr).right != null) {
                        methodName = ((MappingDot) expr).right;
                        expr = ((MappingDot) expr).left;
                    }
                    else if (expr.token == Token.ID) {
                        methodName = (MappingIdentity) expr;
                        expr = null;
                    }
                    if (methodName == null || StringUtils.isEmpty(methodName.getName())) {
                        syntaxErrorWatch1(ch);
                    }
                    expr = parseInvokeParams(variableScope, expr, methodName);
                    match(')');
                    break;
                default:
                    expr = parseLiteral();
                    break;
            }
        }
        return expr;
    }




    public MappingIdentity parseIdentity() {
        MappingExpr expr = parseTextToken();
        matchToken(expr, Token.ID);
        return (MappingIdentity) expr;
    }


    public MappingExpr parseTextToken() {
        char ch = watch(1);
        StringBuilder builder = new StringBuilder();
        if (Character.isAlphabetic(ch) || ch == '_') {
            do {
                builder.append(ch);
                nextChar();
                ch = watch(1);
            } while (Character.isAlphabetic(ch) || Character.isDigit(ch) || ch == '_');
        }
        String text = builder.toString();
        if ("true".equals(text)) {
            return new MappingBoolean(true);
        }
        if ("false".equals(text)) {
            return new MappingBoolean(false);
        }
        return new MappingIdentity(text);
    }


    public MappingIndex parseIndex() {
        char ch = watch(1);
        int index = 0;
        int n = 0;
        while (Character.isDigit(ch)) {
            int x = ch - '0';
            index = 10 * n + x;
            n++;
            nextChar();
            ch = watch(1);
        }
        return new MappingIndex(index);
    }


    public MappingString parseString(char quoteChar) {
        StringBuilder builder = new StringBuilder();
        char ch = watch(1);
        while (ch != quoteChar && !isEnd()) {
            builder.append(ch);
            nextChar();
            ch = watch(1);
        }
        return new MappingString(builder.toString());
    }


    public MappingInvoke parseInvokeParams(VariableScope variableScope, MappingExpr left, MappingIdentity name) {
        return parseMethodParams_inner(variableScope, left, name, new ArrayList<MappingExpr>());
    }


    public MappingInvoke parseMethodParams_inner(VariableScope variableScope, MappingExpr left, MappingIdentity name, List<MappingExpr> argExprList) {
        skipSpaces();
        char ch = watch(1);
        switch (ch) {
            case ')':
                if (left == null) {
                    //return new MappingFilterInvoke(variableScope, name, argExprList);
                }
                return new MappingInvoke(variableScope, left, name, argExprList);
            case ',':
                nextChar();
                MappingExpr expr = parseExpression();
                argExprList.add(expr);
                return parseMethodParams_inner(variableScope, left, name, argExprList);
            default:
                MappingExpr expr2 = parseExpression();
                argExprList.add(expr2);
                return parseMethodParams_inner(variableScope, left, name, argExprList);
        }
    }



    public MappingExpr parseLiteral() {
        char ch = watch(1);
        if (Character.isDigit(ch)) {
            StringBuilder builder = new StringBuilder();
            do {
                builder.append(ch);
                nextChar();
                ch = watch(1);
            } while (Character.isDigit(ch));
            if (ch == '.') {
                char ch2 = watch(2);
                if (Character.isDigit(ch2)) {
                    builder.append('.');
                    nextChar();
                    do {
                        builder.append(ch);
                        nextChar();
                        ch = watch(1);
                    } while (Character.isDigit(ch));
                    if (ch == 'f' || ch == 'F') {
                        nextChar();
                        return new MappingFloat(Float.valueOf(builder.toString()));
                    }
                    if (ch == 'd' || ch == 'D') {
                        nextChar();
                        return new MappingDouble(Double.valueOf(builder.toString()));
                    }
                    return new MappingFloat(Float.valueOf(builder.toString()));
                }
            }
            else if (ch == 'l' || ch == 'L') {
                return new MappingLong(Long.valueOf(builder.toString()));
            }
            else {
                return new MappingInteger(Integer.valueOf(builder.toString()));
            }
        }
        else if (Character.isAlphabetic(ch) || ch == '_') {
            return parseTextToken();
        }
        syntaxErrorWatch1(ch);
        return null;
    }


    public String render(Object[] args) {
        JsonConverter jsonConverter = variableScope.getConfiguration().getJsonConverter();
        int len = exprList.size();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            MappingExpr expr = exprList.get(i);
            Object val = null;
            if (expr instanceof MappingString) {
                builder.append(((MappingString) expr).getText());
            }
            else if (expr instanceof MappingIndex) {
                try {
                    val = args[((MappingIndex) expr).getIndex()];
                } catch (Exception ex) {
                }
                if (val != null) {
                    builder.append(getParameterValue(jsonConverter, val));
                }
            }
            else {
                val = expr.render(args);
                if (val != null) {
                    builder.append(getParameterValue(jsonConverter, val));
                }
            }
        }
        return builder.toString();
    }

    public static String getParameterValue(JsonConverter jsonConverter, Object obj) {
        if (obj == null) {
            return "";
        }
        if (obj instanceof Map || obj instanceof Collection) {
            return jsonConverter.encode(obj);
        }
        if (obj instanceof Date) {
            return String.valueOf(((Date) obj).getTime());
        }
        return obj.toString();
    }

    @Override
    public String toString() {
        StringBuffer buff = new StringBuffer();
        for (MappingExpr expr : exprList) {
            buff.append(expr.toString());
        }
        return buff.toString();
    }

    public MappingTemplate valueOf(String value, MyhttpMethod myhttpMethod) {
        return new MappingTemplate(value, myhttpMethod);
    }
}
