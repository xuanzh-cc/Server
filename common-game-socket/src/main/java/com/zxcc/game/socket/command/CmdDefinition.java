package com.zxcc.game.socket.command;

import java.lang.reflect.Method;

/**
 * Created by xuanzh.cc on 2016/10/10.
 */
public class CmdDefinition {
    /** 命令号 */
    private int cmdId;
    /** 描述 */
    private String desc;
    /** 命令处理方法 */
    private Method method;

    /** 实例化方法 */
    public static CmdDefinition valueOf(int cmdId, String desc, Method method) {
        CmdDefinition cmdDefinition = new CmdDefinition();
        cmdDefinition.cmdId = cmdId;
        cmdDefinition.desc = desc;
        cmdDefinition.method = method;
        return cmdDefinition;
    }

    public int getCmdId() {
        return cmdId;
    }

    public void setCmdId(int cmdId) {
        this.cmdId = cmdId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
