package com.zxcc.socket.core;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.util.ClassUtils;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.MessageLite;
import com.zxcc.game.protobuf.ModuleProtocol.RequestMsg;
import com.zxcc.socket.constanct.ErrorCode;
import com.zxcc.socket.exception.InvokeException;
import com.zxcc.socket.exception.ResultException;
import com.zxcc.socket.filter.session.SessionManagerFilter;
import com.zxcc.socket.model.Result;

/**
 * 方法信息
 * 
 * @author LJJ
 * 
 */
@Sharable
public class MethodInfo implements MethodInvoke
{
	private static final Logger logger = LoggerFactory.getLogger(MethodInfo.class);
	private static HashMap<Class<?>, Class<?>> baseToWrap = new HashMap<>();

	/**
	 * 目标对象
	 */
	private Object target;
	/**
	 * 具体方法
	 */
	private Method method;

	public MethodInfo(Object target, Method method)
	{
		super();
		this.target = target;
		this.method = method;
		baseToWrap.put(byte.class, Byte.class);
		baseToWrap.put(short.class, Short.class);
		baseToWrap.put(int.class, Integer.class);
		baseToWrap.put(long.class, Long.class);
		baseToWrap.put(char.class, Character.class);
		baseToWrap.put(float.class, Float.class);
		baseToWrap.put(double.class, Double.class);
		baseToWrap.put(boolean.class, Boolean.class);
	}

	public Object getTarget()
	{
		return target;
	}

	public void setTarget(Object target)
	{
		this.target = target;
	}

	public Method getMethod()
	{
		return method;
	}

	public void setMethod(Method method)
	{
		this.method = method;
	}

	@Override
	public Result invoke(RequestMsg request, Channel channel)
	{

		Result result = new Result();
		
		try
		{

			Object param1 = null;
			Object[] args = null;
			Class<?>[] clzzs = method.getParameterTypes();

			int lenght = clzzs.length;
			if (lenght > 2 || lenght < 1)
			{
				FormattingTuple message = MessageFormatter.format("对象[{}]的方法[{}]参数不能大于2个,且不能小于1个", target
						.getClass().getName(), method.getName());
				logger.error(message.getMessage());
				throw new InvokeException(message.getMessage());
			}
			else
			{
				Object param2 = null;

				if (clzzs[0].isAssignableFrom(Channel.class))
				{
					param1 = channel;
				}
				else
				{
						param1 = SessionManagerFilter.getIdentity(channel);
						if ( param1 == null)
						{
							FormattingTuple message = MessageFormatter.format("对象[{}]的方法[{}]参数不能为空", target
									.getClass().getName(), method.getName());
							logger.error(message.getMessage());
							result.setCode(ErrorCode.ILLEGAL_NULL_PARAM);
							return result;
						}
				}
				if (lenght == 2)
				{
					Class<?> clz = clzzs[lenght - 1];
					if (clz.isPrimitive() )
					{
						
						Method mt = baseToWrap.get(clz).getMethod("valueOf", String.class);
						param2 = mt.invoke(null, request.getBody().toStringUtf8());
						logger.debug("received Body : " + param2);
					}
					else if ( clz.isAssignableFrom(String.class))
					{
						
						param2 =  request.getBody().toStringUtf8();
						logger.debug("received Body : " + param2);
					}
					else
					{

						try
						{

							Method mt = clzzs[lenght - 1].getMethod("parseFrom", ByteString.class);
							param2 = mt.invoke(null, request.getBody());
							GeneratedMessage pt = (GeneratedMessage) param2;
							logger.debug("received Body : " + pt.toString().replace("\n", "\t"));
							if (pt.getUnknownFields().asMap().size() > 0)
							{
								FormattingTuple message = MessageFormatter.format(
										"对象[{}]的方法[{}]参数异常参数不是对应的protobuf参数", target.getClass().getName(),
										method.getName());
								logger.error(message.getMessage());
								result.setCode(ErrorCode.ILLEGAL_PROTOBUF_PARAM);
								return result;
							}
						}
						catch (ClassCastException e)
						{
							FormattingTuple message = MessageFormatter.format(
									"对象[{}]的方法[{}]参数异常参数必须是Protobuf对象或者是基本类型的包装类",
									target.getClass().getName(), method.getName());
							logger.error(message.getMessage());
							result.setCode(ErrorCode.ILLEGAL_TYPE_PARAM);
							return result;
						}

					}

					args = new Object[] { param1, param2 };
				}
				else
				{
					args = new Object[] { param1 };
				}
			}

			Object ir = method.invoke(target, args);

			if (ir == null)
			{
 				result.setCode(ErrorCode.PROTOBUF);
				return result;
			}
			else if (ir instanceof MessageLite)
			{
				MessageLite msgl = (MessageLite) ir;
				msgl.toString();
				result.setCode(ErrorCode.PROTOBUF);
				logger.debug("send body : " + msgl.toString().replace("\n", ""));
				result.setBody(msgl.toByteString());
				return result;
			}
			else 
			{
				
				if (ClassUtils.isPrimitiveOrWrapper(ir.getClass()))
				{
					result.setCode(ErrorCode.ISPRIMITIVE);
					logger.debug("send body : " + ir);
					result.setBody(ByteString.copyFromUtf8(String.valueOf(ir)));
					return result;
				}
				else if( ir.getClass().isAssignableFrom(String.class))
				{
					result.setCode(ErrorCode.ISPRIMITIVE);
					logger.debug("send body : " + ir);
					result.setBody(ByteString.copyFromUtf8((String)ir));
					return result;
				}
				else
				{
					FormattingTuple message = MessageFormatter.format(
							"对象[{}]的方法[{}]返回值异常返回值必须是Protobuf对象或者是基本类型", target.getClass().getName(),
							method.getName());
					logger.error(message.getMessage());
					result.setCode(ErrorCode.ILLEGAL_RETURN_TYPE_PARAM);
					return result;
				}

			}
		}
		
		catch (NoSuchMethodException  | InvocationTargetException e)
		{
			
			if(e.getCause() instanceof ResultException)
			{
				result.setCode(((ResultException)e.getCause()).getCode());
				logger.error("",((ResultException)e.getCause()));
				logger.error(((ResultException)e.getCause()).getMessage());
				return result;
			}
			FormattingTuple message = MessageFormatter.format("对象[{}]的方法[{}]返回值异常返回值必须是Protobuf对象或者是基本类型", target
					.getClass().getName(), method.getName());
			logger.error(message.getMessage(), e);
			result.setCode(ErrorCode.ILLEGAL_TYPE_PARAM);
			return result;
		}
		catch (Exception  e)
		{
			// if (e instanceof InvokeException) {
			// throw (InvokeException) e;
			// }
			FormattingTuple message = MessageFormatter.format("对象[{}]的方法[{}]访问异常",
					target.getClass().getName(), method.getName());
			logger.error(message.getMessage(), e);
			result.setCode(ErrorCode.UNKNOW_ERROR);
			return result;
		}
		

	}

}
