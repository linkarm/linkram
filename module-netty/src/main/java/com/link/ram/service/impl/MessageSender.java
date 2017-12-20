package com.link.ram.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.link.ram.service.IMessageSender;
import com.link.ram.socket.SocketChannelManager;

import io.netty.buffer.Unpooled;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.CharsetUtil;

@Service
public class MessageSender implements IMessageSender {

	@Override
	public String sendMessageToAll(String msg) {
		Map<String, SocketChannel> channels=SocketChannelManager.getInstance().getAllChannels();
		for(Map.Entry<String, SocketChannel> entry:channels.entrySet()) {
			SocketChannel socketChannel=entry.getValue();
			socketChannel.writeAndFlush(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
		}
		return "success";
	}
}
