package com.zxcc.socket.constanct;

public interface ErrorCode
{



	/**
	 * 参数非法
	 */
	int ILLEGAL_PARAM = -251;
	/**
	 * 参数不能为空
	 */
	int ILLEGAL_NULL_PARAM = -252;
	/**
	 * 返回值异常返回值必须是Protobuf对象或者是基本类型
	 */
	int ILLEGAL_RETURN_TYPE_PARAM = -253;

	/**
	 * 参数异常参数必须是Protobuf对象或者是基本类型的包装类
	 */
	int ILLEGAL_TYPE_PARAM = -254;
	/**
	 * 未知错误
	 */
	int UNKNOW_ERROR = -255;
	/**
	 * 参数异常参数不是对应的protobuf参数
	 */
	int ILLEGAL_PROTOBUF_PARAM = -256;
	
	/**
	 *钻石不足
	 */
	int STONE_NOT_ENOUGH = -257;

	/**
	 * 金币不足
	 */
	int GOLD_NOT_ENOUGH = -258;
	/**
	 * 返回参数是protobuf类型
	 */
	int PROTOBUF = 0;
	/**
	 * 返回基础类型
	 */
	int ISPRIMITIVE = 1;
	/**
	 * 返回参数是空
	 */
	int NULL = 2;
	
	
	

	
	
}
