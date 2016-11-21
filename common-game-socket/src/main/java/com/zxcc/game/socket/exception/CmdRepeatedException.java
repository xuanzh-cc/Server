package com.zxcc.game.socket.exception;

/**
 * Created by xuanzh.cc on 2016/10/10.
 */
public class CmdRepeatedException extends RuntimeException {

    public CmdRepeatedException() {
    }

    public CmdRepeatedException(String message) {
        super(message);
    }

    public CmdRepeatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
