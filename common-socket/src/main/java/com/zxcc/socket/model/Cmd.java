package com.zxcc.socket.model;


/**
 * 通信指令
 * 
 * @author LJJ
 */
public class Cmd {

	/** 指令所属模块 */
	private Mod module;
	/** 指令标识 */
	private int command;

	/**
	 * 创建通信指令对象实例
	 * 
	 * @param command
	 *            指令标识
	 * @param modules
	 *            所属模块标识
	 * @return
	 */
	public static Cmd valueOf(int command, int modules) {
		Cmd result = new Cmd();
		result.command = command;
		result.module = Mod.valueOf(modules);
		return result;
	}
	

	public Mod getModule() {
		return module;
	}


	public void setModule(Mod module) {
		this.module = module;
	}


	public int getCommand() {
		return command;
	}


	public void setCommand(int command) {
		this.command = command;
	}


	@Override
	public String toString() {
		return "Command [module=" + module + ", command=" + command + "]";
	}
	
	
	public String getModAndCmd()
	{
		return getModule().getId() + "_" + getCommand();
	}
	
	
	

}
