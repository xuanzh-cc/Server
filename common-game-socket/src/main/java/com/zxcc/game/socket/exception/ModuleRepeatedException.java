package com.zxcc.game.socket.exception;

/**
 * Created by xuanzh.cc on 2016/10/10.
 */
public class ModuleRepeatedException extends RuntimeException{

    public ModuleRepeatedException() {
    }

    public ModuleRepeatedException(String message) {
        super(message);
    }

    public ModuleRepeatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
