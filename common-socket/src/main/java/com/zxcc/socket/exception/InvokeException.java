package com.zxcc.socket.exception;

/**
 * 执行异常
 * 
 * @author LJJ
 */
public class InvokeException extends SocketException {

	private static final long serialVersionUID = 3027236671106441923L;

	public InvokeException() {
		super();
	}

	public InvokeException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvokeException(String message) {
		super(message);
	}

	public InvokeException(Throwable cause) {
		super(cause);
	}

}
