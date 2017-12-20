package com.link.ram.socket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.socket.SocketChannel;

/**
 * 
 * Socket Channel 管理器
 * @author zk
 *
 */
public class SocketChannelManager {
	private final static String SOCKET_MANGER_INSTANCE="SOCKET_MANGER_INSTANCE";
	private static Map<String,SocketChannelManager> instanceMap=new HashMap<String,SocketChannelManager>();
	
	private final Map<String,SocketChannel> allChannels=new ConcurrentHashMap<String,SocketChannel>();
	
	private SocketChannelManager() {};
	
	static{
		SocketChannelManager socketManager=new SocketChannelManager();
		instanceMap.put(SOCKET_MANGER_INSTANCE, socketManager);
	}
	
	public static SocketChannelManager getInstance() {
		return instanceMap.get(SOCKET_MANGER_INSTANCE);
	}
	
	public void registerChannel(String terminatorId,SocketChannel socketChannel) {
		allChannels.put(terminatorId, socketChannel);
	}
	
	public void removeChannel(String terminatorId) {
		allChannels.remove(terminatorId);
	}
	
	public SocketChannel getChannel(String terminatorId) {
		return allChannels.get(terminatorId);
	}
	
	public Map<String,SocketChannel> getAllChannels(){
		return allChannels;
	}
}
