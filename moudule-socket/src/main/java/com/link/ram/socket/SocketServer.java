package com.link.ram.socket;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.util.CollectionUtils;

/**
 * nio socket服务端
 */
public class SocketServer {
    //解码buffer
    private Charset cs = Charset.forName("UTF-8");
    //接受数据缓冲区
    private static ByteBuffer sBuffer = ByteBuffer.allocate(1024);
    //发送数据缓冲区
    private static ByteBuffer rBuffer = ByteBuffer.allocate(1024);
    //选择器（叫监听器更准确些吧应该）
    private static Selector selector;
    
    private Map<String,SocketChannel> scMaps=new HashMap<String,SocketChannel>();
    
    private static SocketServer instance;

    private SocketServer() {
    	
    }
    
    public static SocketServer getSocketServerInstance(){
    	if(instance!=null) {
    		return instance;
    	}else {
    		instance=new SocketServer();
    		instance.initSocket();
    		return instance;
    	}
    }
    /**
     * 启动socket服务，开启监听
     * @param port
     * @throws IOException
     */
    private void initSocket(){
        try {
            //打开通信信道
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            //获取套接字
            ServerSocket serverSocket = serverSocketChannel.socket();
            //绑定端口号
            serverSocket.bind(new InetSocketAddress(8090));
            //打开监听器
            selector = Selector.open();
            //将通信信道注册到监听器
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            //监听器会一直监听，如果客户端有请求就会进入相应的事件处理
            while (true){
                selector.select();//select方法会一直阻塞直到有相关事件发生或超时
                Set<SelectionKey> selectionKeys = selector.selectedKeys();//监听到的事件
                for (SelectionKey key : selectionKeys) {
                    handle(key);
                }
                selectionKeys.clear();//清除处理过的事件
            }
        }catch (Exception e){
            e.printStackTrace();
        }



    }
    
    public void sendMessageToAll(String msg) throws IOException {
    	if(!CollectionUtils.isEmpty(scMaps)) {
    		for(Map.Entry<String,SocketChannel> entry:scMaps.entrySet()){
    			SocketChannel chanel=entry.getValue();
    			writeMsgFromChannel(msg, chanel);
    		}
    	}
    }
	private void writeMsgFromChannel(String msg, SocketChannel chanel)
			throws UnsupportedEncodingException, IOException {
		sBuffer.clear();
		sBuffer = ByteBuffer.allocate(msg.getBytes("UTF-8").length);
		sBuffer.put(msg.getBytes("UTF-8"));
		sBuffer.flip();
		chanel.write(sBuffer);
	}
    /**
     * 处理不同的事件
     * @param selectionKey
     * @throws IOException
     */
    private void handle(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isAcceptable()) {
            //每有客户端连接，即注册通信信道为可读
            registerSocketChannel(selectionKey);
        }
        else if (selectionKey.isReadable()) {
            handleMessage(selectionKey);
        }
    }
    /**
     * 处理消息
     * @param selectionKey
     */
	private void handleMessage(SelectionKey selectionKey) {
		SocketChannel socketChannel=null;
		String requestMsg=null;
		int count=0;
		try {
			socketChannel = (SocketChannel)selectionKey.channel();
			rBuffer.clear();
			count = socketChannel.read(rBuffer);
			//读取数据
			if (count > 0) {
			    rBuffer.flip();
			    requestMsg = String.valueOf(cs.decode(rBuffer).array());
			    String responseMsg = "receive data from serve: "+requestMsg;
			    sendMessageToAll(responseMsg);
			}else{
				socketChannel.finishConnect();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 注册终端设备和写缓存设备连接会话
	 * @param selectionKey
	 * @throws IOException
	 * @throws ClosedChannelException
	 */
	private void registerSocketChannel(SelectionKey selectionKey) throws IOException, ClosedChannelException {
		ServerSocketChannel serverSocketChannel;
		SocketChannel socketChannel;
		serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
		socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		socketChannel.register(selector, SelectionKey.OP_READ);
		String channelKey=socketChannel.getRemoteAddress().toString();
		scMaps.put(channelKey, socketChannel);
	}
}