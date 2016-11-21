package com.zxcc.resources.exception;

/**
 * Created by xuanzh.cc on 2016/6/27.
 */
public class DecodeException extends RuntimeException {

    private static final long serialVersionUID = -8963056200705256870L;

    public DecodeException() {
    }

    public DecodeException(String message) {
        super(message);
    }

    public DecodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DecodeException(Throwable cause) {
        super(cause);
    }
}
