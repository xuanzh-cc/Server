package com.zxcc.event;

import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public abstract class BaseListener<T extends Event> {
	
	@Autowired
	private EventBus eventBus;
	@PostConstruct
	public void init()
	{
		eventBus.register(this);
	}
	
	/**
	 * 事件处理方法
	 * 
	 * @param event
	 *            事件消息体
	 */
	public abstract void doEvent(T event);
	

}
