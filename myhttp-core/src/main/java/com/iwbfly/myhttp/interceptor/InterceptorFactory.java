package com.iwbfly.myhttp.interceptor;

import com.iwbfly.myhttp.exceptions.MyhttpRuntimeException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @auther: pangyajun
 * @create: 2021/11/22 12:45
 **/
public interface InterceptorFactory {

    InterceptorChain getInterceptorChain();

    <T extends Interceptor> T getInterceptor(Class<T> clazz);

    class Default implements InterceptorFactory{

        protected final Map<Class, Interceptor> interceptorMap = new ConcurrentHashMap<>();

        protected InterceptorChain interceptorChain = new InterceptorChain();

        @Override
        public InterceptorChain getInterceptorChain() {
            return interceptorChain;
        }

        @Override
            public <T extends Interceptor > T getInterceptor(Class < T > clazz) {
                Interceptor interceptor = interceptorMap.get(clazz);
                if (interceptor == null) {
                    synchronized (this) {
                        interceptor = interceptorMap.get(clazz);
                        if (interceptor == null) {
                            interceptor = createInterceptor(clazz);
                        }
                    }
                }
                return (T) interceptor;
            }

        protected <T extends Interceptor> Interceptor createInterceptor(Class<T> clazz) {
            Interceptor interceptor;
            try {
                interceptor = clazz.newInstance();
                interceptorMap.put(clazz, interceptor);
            } catch (InstantiationException e) {
                throw new MyhttpRuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new MyhttpRuntimeException(e);
            }
            return interceptor;
        }
        }
}
