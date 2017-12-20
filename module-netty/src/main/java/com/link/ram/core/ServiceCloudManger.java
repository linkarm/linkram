package com.link.ram.core;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.link.ram.socket.SocketChannelHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 
 * @author zk
 *
 */
@Component
public class ServiceCloudManger implements ApplicationContextAware {
	
  private static final Logger logger = LoggerFactory.getLogger(ServiceCloudManger.class);
  
  private static ApplicationContext context;
  
  private Channel channel;
  private EventLoopGroup bossGroup;
  private EventLoopGroup workerGroup;

  @Value("${tcp.server.host:192.168.1.105}")
  String host;

  @Value("${tcp.srver.ioThreadNum:5}")
  int ioThreadNum;
  //内核为此套接口排队的最大连接个数，对于给定的监听套接口，内核要维护两个队列，未链接队列和已连接队列大小总和最大值

  @Value("${tcp.server.backlog:1024}")
  int backlog;

  @Value("${tcp.server.port:8090}")
  int port;

  /**
   * 启动
   * @throws InterruptedException
   */
  @PostConstruct
  public void start() throws InterruptedException {
      logger.info("begin to start rpc server");
      bossGroup = new NioEventLoopGroup();
      workerGroup = new NioEventLoopGroup(ioThreadNum);

      ServerBootstrap serverBootstrap = new ServerBootstrap();
      serverBootstrap.group(bossGroup, workerGroup)
              .channel(NioServerSocketChannel.class)
              .childOption(ChannelOption.SO_KEEPALIVE, true)
              .option(ChannelOption.SO_BACKLOG, 128) //设置TCP缓冲区
              .option(ChannelOption.SO_SNDBUF, 32 * 1024) //设置发送数据缓冲大小
              .option(ChannelOption.SO_RCVBUF, 32 * 1024) //设置接受数据缓冲大小
              .childHandler(new ChannelInitializer<SocketChannel>() {
                  @Override
                  protected void initChannel(SocketChannel socketChannel) throws Exception {
                	 
                	  socketChannel.pipeline()
                              //真实数据最大字节数为Integer.MAX_VALUE，解码时自动去掉前面四个字节
                      			.addLast("handler",new IdleStateHandler(3, 0, 0, TimeUnit.SECONDS))
                      			.addLast(new SocketChannelHandler());
                  }
              });

      channel = serverBootstrap.bind(host,port).sync().channel();
     
      logger.info("NettyRPC server listening on port " + port + " and ready for connections...");
  }

  @PreDestroy
  public void stop() {
      logger.info("destroy server resources");
      if (null == channel) {
          logger.error("server channel is null");
      }
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
      channel.closeFuture().syncUninterruptibly();
      bossGroup = null;
      workerGroup = null;
      channel = null;
  }

  /**
   * 利用此方法获取spring ioc接管的所有bean
   * @param ctx
   * @throws BeansException
   */
  public void setApplicationContext(ApplicationContext ctx) throws BeansException {
	  if(context==null) {
		  ServiceCloudManger.context=ctx;
	  }
  }
  
  public static ApplicationContext getApplicationContext() {
	  return context;
  }
}
