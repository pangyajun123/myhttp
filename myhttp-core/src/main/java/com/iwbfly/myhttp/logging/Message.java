package com.iwbfly.myhttp.logging;

import java.util.Collection;
import java.util.Map;

/**
 * @auther: pangyajun
 * @create: 2021/12/3 20:47
 **/
public class Message {

    private String message;

    public Map<String, Collection<String>> variable;

    public Message(String message) {
        this.message = message;
    }

    public Message(Map<String, Collection<String>> variable) {
        this.variable = variable;
    }

    public Message() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;

    }

    public Map<String, Collection<String>> getVariable() {
        return variable;
    }

    public void setVariable(Map<String, Collection<String>> variable) {
        this.variable = variable;
    }
}
