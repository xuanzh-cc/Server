package com.zxcc.event;

/**
 * 事件对象
 * 
 */
public class Event {

	/**
	 * 玩家id
	 */
	protected long owner;

	public long getOwner()
	{
		return owner;
	}

	public void setOwner(long owner)
	{
		this.owner = owner;
	}
	
	
}
