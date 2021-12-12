package com.iwbfly.myhttp.exceptions;

/**
 * @auther: pangyajun
 * @create: 2021/11/22 12:40
 **/
public class MyhttpRuntimeException extends RuntimeException{

    private int status;
    private byte[] content;

    public MyhttpRuntimeException(String message) {
        super(message);
    }

    public MyhttpRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyhttpRuntimeException(Throwable cause) {
        super(cause);
    }
}
