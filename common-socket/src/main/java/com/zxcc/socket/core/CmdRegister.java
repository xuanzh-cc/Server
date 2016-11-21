package com.zxcc.socket.core;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.zxcc.socket.anno.Command;
import com.zxcc.socket.anno.Module;
import com.zxcc.socket.exception.MethodInvokeNotFound;
import com.zxcc.socket.model.Cmd;
import com.zxcc.socket.util.AnnotationUtility;

/**
 * 指令注册器工厂
 * 
 * @author LJJ
 */
@Component
public class CmdRegister implements BeanPostProcessor {

	private static final Logger logger = LoggerFactory.getLogger(CmdRegister.class);

	
	/** mod_cmd -> MethodInfo 映射表*/
	private ConcurrentHashMap<String,MethodInfo> methodMethodInfo = new ConcurrentHashMap<String, MethodInfo>();


	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		

		Class<?> clazz = AnnotationUtility.findAnnotationDeclaringClassAndInterface(
				Module.class, bean.getClass());
		if (clazz == null) {
			return bean;
		}

		Module handler = clazz.getAnnotation(Module.class);
		if (handler != null) {
//			byte modules = handler.value();
			for (Method method : clazz.getMethods()) {
				Command Command = AnnotationUtility.findAnnotation(method, Command.class);
				if (Command == null) {
					// 不是指令方法，跳过不处理
					continue;
				}

				// 创建指令对象
				Cmd command = generatorComm(Command, handler);
				
				// 进行注册
				try {
					register(command, clazz, bean, method);
				}
				catch (Exception e) {
					FormattingTuple message = MessageFormatter.format("注册指令[{}]到对应方法[{}]时失败",
							command, method);
					logger.error(message.getMessage());
					throw new FatalBeanException(message.getMessage(), e);
				}
			}
		}
		return bean;
	}

	// 指令注册方法

	public void register(Cmd cmd, Class<?> clz, Object bean, Method method)
			throws Exception {
		
//		MethodProcessor handler = MethodProcessor.valueOf(clz, bean, method, builder);
		String name = bean.toString();
		name = name.substring(0, name.indexOf('@'));
		MethodInfo methodInfo = new MethodInfo(bean, method);
		MethodInfo prev =methodMethodInfo.putIfAbsent(cmd.getModAndCmd(), methodInfo);
		
		if (prev != null) {
			FormattingTuple message = MessageFormatter.format("指令[{}]重复注册:{}", cmd, prev);
			logger.error(message.getMessage());
			throw new IllegalStateException(message.getMessage());
		}

		logger.debug("完成指令注册:{}", cmd.getModAndCmd() +", method : " +bean.getClass().getName() +"."+ method.getName());
//		getObject().register(command, handler.getDefinition(), handler);
//		getObject().getMethodCmds().putIfAbsent(name + "."+ method.getName(), command);
	}
	
	
	
	private  Cmd generatorComm(Command Command ,Module module)
	{
		Cmd command = null;
		if(Command.value() == 0)
		{
			String message = "Command不能没有内容或者为0";
			logger.error(message);
			throw new IllegalArgumentException(message);
		}
		else if (Command.modules() != 0) {
			command = Cmd.valueOf(Command.value(), Command.modules());
		}
		else {
			if(module.value() != 0)
			{
				command = Cmd.valueOf(Command.value(), module.value() );
			}
			else
			{
				String message = "modules不能没有内容或者为0";
				logger.error(message);
				throw new IllegalArgumentException(message);
			}
			
			
		}
		return command;
	}

//	private void registerFromClass(Class<?> clz) throws Exception {
//		Module handler = clz.getAnnotation(Module.class);
//		for (Method method : clz.getMethods()) {
//			if (method.getAnnotation(Command.class) == null) {
//				continue; // 忽略无效的方法
//			}
//
//			Command Command = method.getAnnotation(Command.class);
//			
//			// 创建指令对象
//			Cmd command = generatorComm(Command, handler);
//			// 创建指令对象
//			// 创建指令对象
//			// 创建指令对象
//			
//			// 创建类型定义
////			MethodDefinition definition = MethodDefinition.valueOf(clz, method, builder);
//			// 进行注册
////			this.getObject().register(command, definition, null);
//		}
//	}

	/**
	 * 获取对应的{@link MethodInvoke}
	 * 
	 * @param cmd
	 *            指令
	 * @return 指定对应的执行器，不会返回null
	 * @throws MethodInvokeNotFound
	 *             执行器不存在时抛出
	 */
	public MethodInvoke getProcessor(Cmd cmd) {
		MethodInvoke info = methodMethodInfo.get(cmd.getModAndCmd());
		if (info == null ) {
			FormattingTuple message = MessageFormatter.format("指令[{}]对应的业务执行器不存在", cmd);
			if (logger.isDebugEnabled()) {
				logger.debug(message.getMessage());
			}
			throw new MethodInvokeNotFound(message.getMessage());
		}
		return info;
	}
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

}
