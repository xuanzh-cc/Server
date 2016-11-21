package com.zx.socket.test;

import com.zx.socket.test.handler.StringDecode;
import com.zx.socket.test.handler.StringEncode;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by xuanzh.cc on 2016/7/26.
 */
public class Client {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap client = new Bootstrap();
        client.group(new NioEventLoopGroup());
        client.channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new StringDecode());
                        p.addLast(new StringEncode());
                    }
                });

        Channel channel = client.connect(new InetSocketAddress("127.0.0.1", 9999)).sync().channel();

        if(channel.isOpen()) {
            channel.writeAndFlush("啊哈哈哈哈哈哈哈！\r\n/n").sync();
        }
    }
}
