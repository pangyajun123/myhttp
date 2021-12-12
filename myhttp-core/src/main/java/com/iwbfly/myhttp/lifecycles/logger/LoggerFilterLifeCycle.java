package com.iwbfly.myhttp.lifecycles.logger;

import com.iwbfly.myhttp.lifecycles.LoggerAnnotationLifeCycle;
import com.iwbfly.myhttp.lifecycles.MethodAnnotationLifeCycle;
import com.iwbfly.myhttp.logging.Message;

import java.util.*;

/**
 * @auther: pangyajun
 * @create: 2021/12/3 18:17
 **/
public class LoggerFilterLifeCycle implements LoggerAnnotationLifeCycle {
    @Override
    public void requestLog(Message message) {
        System.out.println("请求日志过滤");
    }

    @Override
    public void responseLog(Message message) {
        System.out.println("响应日志过滤");
        Map<String, Collection<String>> headersMap = message.getVariable();
        if (headersMap != null) {
            Iterator<Map.Entry<String, Collection<String>>> it = headersMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Collection<String>> entry = it.next();
                if ("content-type".equals(entry.getKey().toLowerCase(Locale.ROOT))) {
                    it.remove();
                }
            }
        }
        String str = message.getMessage();
        if (str != null)
            str = str.replaceAll("\"password\":\"(\\w+)\"", "\"password\":\"******\"");
        message.setMessage(str);
    }

}
