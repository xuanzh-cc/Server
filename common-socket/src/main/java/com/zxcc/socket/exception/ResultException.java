package com.zxcc.socket.exception;

public class ResultException extends RuntimeException {

	
	private static final long serialVersionUID = -2130134158292564005L;

	private final int code;
	

	public ResultException(String message, int code, Throwable cause) {
		
		super(message, cause);
		this.code = code;
	}

	public ResultException(String message, int code) {
		super(message);
		this.code = code;
	}

	public int getCode()
	{
		return code;
	}

	
}