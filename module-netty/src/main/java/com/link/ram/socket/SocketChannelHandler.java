package com.link.ram.socket;

import com.link.ram.core.ServiceCloudManger;
import com.link.ram.service.IMessageSender;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;

public class SocketChannelHandler extends ChannelHandlerAdapter {
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			//do something msg
			ByteBuf buf = (ByteBuf)msg;
			byte[] data = new byte[buf.readableBytes()];
			buf.readBytes(data);
			String requestMsg = new String(data, "utf-8");
			System.out.println("Server: " + requestMsg);
			IMessageSender messageSender=ServiceCloudManger.getApplicationContext().getBean(IMessageSender.class);
			messageSender.sendMessageToAll(requestMsg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		SocketChannel socketChannel=(SocketChannel) ctx.channel();
		SocketChannelManager.getInstance().registerChannel(socketChannel.remoteAddress().getHostString()+":"+socketChannel.remoteAddress().getPort(), socketChannel);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		SocketChannel socketChannel=(SocketChannel) ctx.channel();
		SocketChannelManager.getInstance().removeChannel(socketChannel.remoteAddress().getHostString()+":"+socketChannel.remoteAddress().getPort());
	}

	
}
