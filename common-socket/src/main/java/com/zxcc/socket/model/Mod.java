package com.zxcc.socket.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

/**
 * 指令模块声明
 * 
 * @author LJJ
 */
public class Mod {

	private static final Logger logger = LoggerFactory.getLogger(Mod.class);

	/** 模块标识 */
	private int id;

	public static Mod valueOf(int moduleId) {

		if (moduleId < Byte.MIN_VALUE || moduleId > Byte.MAX_VALUE) {
			FormattingTuple message = MessageFormatter.format("构造参数值必须在[{}]到[{}]之间",
					Byte.MIN_VALUE, Byte.MAX_VALUE);
			logger.error(message.getMessage());
			throw new IllegalArgumentException(message.getMessage());
		}
		
		Mod result = new Mod();
		result.id = moduleId;
		return result;

	}

	


	public int getId() {
		return id;
	}




	@Override
	public String toString() {
		return "Mod [id=" + id + "]";
	}

	
}
