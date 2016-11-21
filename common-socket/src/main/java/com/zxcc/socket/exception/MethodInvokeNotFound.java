package com.zxcc.socket.exception;

/**
 * 执行器不存在的异常
 * 
 * @author LJJ
 */
public class MethodInvokeNotFound extends SocketException {

	private static final long serialVersionUID = -4349222660303288826L;

	public MethodInvokeNotFound() {
		super();
	}

	public MethodInvokeNotFound(String message, Throwable cause) {
		super(message, cause);
	}

	public MethodInvokeNotFound(String message) {
		super(message);
	}

	public MethodInvokeNotFound(Throwable cause) {
		super(cause);
	}

}
