package com.zxcc.game.socket.exception;

/**
 * Created by xuanzh.cc on 2016/10/10.
 */
public class InvokeException extends RuntimeException {

    public InvokeException() {
    }

    public InvokeException(String message) {
        super(message);
    }

    public InvokeException(String message, Throwable cause) {
        super(message, cause);
    }
}
