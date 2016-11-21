package com.zxcc.socket.event;

import com.zxcc.event.Event;

import io.netty.channel.Channel;


public class ChannelCloseEvent  extends Event{
	
	private int cause;
	private Channel channel;
	
	

	public  static ChannelCloseEvent valueOf(Object id, int cause,Channel channel) {
		ChannelCloseEvent result = new ChannelCloseEvent();
		result.owner = (long) id;
		result.cause = cause;
		result.channel = channel;
		return result;
	}


	public int getCause() {
		return cause;
	}

	public void setCause(int cause) {
		this.cause = cause;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

}
