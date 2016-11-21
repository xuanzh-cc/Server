package com.zxcc.socket.util;

import io.netty.channel.Channel;
import org.apache.commons.lang.StringUtils;

public class IpUtils {
	/**
	 * 获取会话的IP地址
	 * 
	 * @param channel
	 * @return
	 */
	public static String getIp(Channel channel) {
		if (channel == null || channel.remoteAddress()== null) {
			return "UNKNOWN";
		}
		String ip = channel.remoteAddress().toString();
		return StringUtils.substringBetween(ip, "/", ":");
	}
}
