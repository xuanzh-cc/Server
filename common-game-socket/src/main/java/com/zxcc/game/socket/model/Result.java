package com.zxcc.game.socket.model;

import com.google.protobuf.ByteString;

/**
 * Created by xuanzh.cc on 2016/10/11.
 */
public class Result {
    private int code;
    private ByteString byteString;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ByteString getByteString() {
        return byteString;
    }

    public void setByteString(ByteString byteString) {
        this.byteString = byteString;
    }
}
