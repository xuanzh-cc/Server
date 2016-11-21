package com.zxcc.game.socket.command;

import java.util.HashMap;
import java.util.Map;

/**
 * 模块
 * Created by xuanzh.cc on 2016/10/10.
 */
public class ModuleDefinition {

    private int moduleId;

    private Object handler;

    private Map<Integer, CmdDefinition> cmdMap;

    public static ModuleDefinition valueOf(int moduleId, Object handler) {
        ModuleDefinition moduleDefinition = new ModuleDefinition();
        moduleDefinition.moduleId = moduleId;
        moduleDefinition.handler = handler;
        moduleDefinition.cmdMap = new HashMap<Integer, CmdDefinition>();
        return moduleDefinition;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public Object getHandler() {
        return handler;
    }

    public void setHandler(Object handler) {
        this.handler = handler;
    }

    public Map<Integer, CmdDefinition> getCmdMap() {
        return cmdMap;
    }

    public void setCmdMap(Map<Integer, CmdDefinition> cmdMap) {
        this.cmdMap = cmdMap;
    }

    /**
     * 根据 cmdId 获取 cmd
     * @param cmdId
     * @return
     */
    public CmdDefinition getCmd(Integer cmdId) {
        return this.cmdMap.get(cmdId);
    }

    /**
     * 添加 cmd
     * @param cmdDefinition
     */
    public void addCmd(CmdDefinition cmdDefinition) {
        this.cmdMap.put(cmdDefinition.getCmdId(), cmdDefinition);
    }
}
