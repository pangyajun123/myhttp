package com.iwbfly.myhttp.exceptions;

import com.iwbfly.myhttp.reflection.MyhttpRequest;

/**
 * @auther: pangyajun
 * @create: 2021/11/25 21:31
 **/
public class RetryableException extends MyhttpRuntimeException{

    private static final long serialVersionUID = 1L;

    public RetryableException(Throwable cause, String message) {
        super(cause.getMessage(), cause);

    }


}
