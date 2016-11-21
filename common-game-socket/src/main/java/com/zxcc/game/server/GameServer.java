package com.zxcc.game.server;

import com.zxcc.game.socket.handler.ChannelHandlerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 服务器
 * Created by xuanzh.cc on 2016/10/10.
 */
@Component
public class GameServer {
    private static Logger logger = LoggerFactory.getLogger(GameServer.class);

    @Value("${server.ip}")
    private String ip = "127.0.0.1";
    @Value("${server.port}")
    private int port = 25800;

    private NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private NioEventLoopGroup childGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);

    @Autowired
    private ChannelHandlerInitializer channelHandlerInitializer;

    private ServerBootstrap server;

    public void start(){
        logger.error("开始启动服务器.....");

        try {
            this.server = new ServerBootstrap();
            this.server.group(bossGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(channelHandlerInitializer);

            this.server.bind(ip, port).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}
