package com.zxcc.game.socket.command;

import com.zxcc.game.socket.exception.CmdRepeatedException;
import com.zxcc.game.socket.annotation.Cmd;
import com.zxcc.game.socket.annotation.Module;
import com.zxcc.game.socket.exception.ModuleRepeatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 命令注册处理器
 * Created by xuanzh.cc on 2016/10/10.
 */
@Component
public class CommandRegisterHolder implements BeanPostProcessor {

    private static Logger logger = LoggerFactory.getLogger(CommandRegisterHolder.class);

    private Map<Integer, ModuleDefinition> moduleMap = new HashMap<Integer, ModuleDefinition>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class clazz = bean.getClass();
        Module module = (Module) clazz.getAnnotation(Module.class);
        if (module != null) {
            try {
                this.registerCommand(module, bean);
            } catch (ModuleRepeatedException e) {
                e.printStackTrace();
            } catch (CmdRepeatedException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

    /**
     * 注册模块 和 命令
     * @param module
     * @param bean
     * @throws ModuleRepeatedException
     * @throws CmdRepeatedException
     */
    private void registerCommand(final Module module, Object bean) throws ModuleRepeatedException, CmdRepeatedException {

        Class clazz = bean.getClass();

        int moduleId = module.value();

        ModuleDefinition moduleDefinition = ModuleDefinition.valueOf(moduleId, bean);

        ModuleDefinition oldModule = moduleMap.get(moduleId);
        //模块号重复
        if(oldModule != null) {
            FormattingTuple message = MessageFormatter.format("模块类型{} 与 {} 冲突， 模块号为{}",
                    new Object[]{clazz.getName(), oldModule.getHandler().getClass().getName(), moduleId});
            logger.error(message.getMessage());
            throw new ModuleRepeatedException(message.getMessage());
        }

        logger.error("完成注册模块{}, 类型{}", moduleId, clazz.getName());
        moduleMap.put(moduleId, moduleDefinition);

        for(Method method : clazz.getMethods()) {
            Cmd cmd = method.getAnnotation(Cmd.class);
            if (cmd == null) {
                return;
            }
            int cmdId = cmd.value();
            String desc = cmd.desc();

            CmdDefinition oldCmd = moduleDefinition.getCmd(cmdId);
            //命令号 重复
            if (oldCmd != null) {
                FormattingTuple message = MessageFormatter.format("模块{}中，命令{} 与 {}冲突，命令号为：{}",
                        new Object[]{moduleId, oldCmd.getMethod().getName(), method.getName(), cmdId});
                logger.error(message.getMessage());
                throw new CmdRepeatedException(message.getMessage());
            }

            CmdDefinition cmdDefinition = CmdDefinition.valueOf(cmdId, desc, method);

            logger.error("完成注册模块{} 命令注册，命令号{}， 方法名称{}", new Object[]{moduleId, cmdId, method.getName()});
            moduleDefinition.addCmd(cmdDefinition);
        }
    }

    /**
     * 获取 指令
     * @param moduleId
     * @param cmdId
     * @return
     */
    public CmdDefinition getCmd(int moduleId, int cmdId) {

        ModuleDefinition moduleDefinition = this.moduleMap.get(moduleId);

        return moduleDefinition == null ? null : moduleDefinition.getCmd(cmdId);

    }
}
