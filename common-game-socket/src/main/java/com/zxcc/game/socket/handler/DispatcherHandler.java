package com.zxcc.game.socket.handler;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;
import com.zxcc.game.socket.command.CmdDefinition;
import com.zxcc.game.socket.command.CommandRegisterHolder;
import com.zxcc.game.socket.exception.InvokeException;
import com.zxcc.game.socket.protobuf.ModuleProtocol.Header;
import com.zxcc.game.socket.protobuf.ModuleProtocol.RequestMsg;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuanzh.cc on 2016/10/10.
 */
@Component
public class DispatcherHandler extends SimpleChannelInboundHandler<RequestMsg> {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherHandler.class);

    private static Map<Class<?>, Class<?>> baseToWrap = new HashMap<Class<?>, Class<?>>();

    @Autowired
    private CommandRegisterHolder commandRegisterHolder;

    static {
        baseToWrap.put(byte.class, Byte.class);
        baseToWrap.put(short.class, Short.class);
        baseToWrap.put(int.class, Integer.class);
        baseToWrap.put(long.class, Long.class);
        baseToWrap.put(char.class, Character.class);
        baseToWrap.put(float.class, Float.class);
        baseToWrap.put(double.class, Double.class);
        baseToWrap.put(boolean.class, Boolean.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMsg msg) throws Exception {
        logger.error("收到消息：" + msg.toString());

        Header header = msg.getHeader();

        int moduleId = header.getMod();
        int cmdId = header.getCmd();

        this.dispatch(moduleId, cmdId, msg.getBody(), ctx.channel());
    }

    /**
     * 消息派发
     * @param moduleId
     * @param cmdId
     * @param byteString
     * @param channel
     */
    private void dispatch(int moduleId, int cmdId, ByteString byteString, Channel channel){
        CmdDefinition cmdDefinition = commandRegisterHolder.getCmd(moduleId, cmdId);

        if (cmdDefinition == null) {
            logger.error("模块号为{}的命令{} 处理逻辑不存在!", moduleId, cmdId);
            channel.disconnect();
        }

        Method method = cmdDefinition.getMethod();

        Class<?>[] parameterTypes = method.getParameterTypes();

        if (parameterTypes.length > 2 || parameterTypes.length < 1) {
            FormattingTuple message = MessageFormatter.format("模块号为{}的命令[{}]处理方法[{}]参数不能少于1个或者大于2个", new Object[]{ moduleId, cmdId, method.getName()});
            logger.error(message.getMessage());
            throw new InvokeException(message.getMessage());
        }

        Object[] params = new Object[parameterTypes.length];

        try {
            //参数1
            Class paramType1 = parameterTypes[0];
            if (paramType1.isAssignableFrom(Channel.class)) {
                params[0] = channel;
            } else {

            }

            if (params.length == 2) {
                Class paramType2 = parameterTypes[1];
                if (ClassUtils.isPrimitiveOrWrapper(paramType2)) {
                    Method convertMethod =  null;
                    if (paramType2.isPrimitive()) {
                        convertMethod = baseToWrap.get(paramType2).getMethod("valueOf", String.class);
                    } else {
                        convertMethod = paramType2.getMethod("valueOf", String.class);
                    }
                    params[0] = convertMethod.invoke(null, byteString.toStringUtf8());
                } else if (paramType2.isAssignableFrom(String.class)){
                    params[1] = byteString.toStringUtf8();
                } else {
                    try{
                        Method parseMethod = paramType2.getMethod("parseFrom", ByteString.class);
                        params[1] = parseMethod.invoke(null, byteString);

                        GeneratedMessage generatedMessage = (GeneratedMessage)params[1];
                        if (generatedMessage.getUnknownFields().asMap().size() > 0) {

                        }

                    } catch (ClassCastException e) {

                    }
                }
            }
        }
        //调用异常
        catch (NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        //方法访问异常（权限不够）
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
