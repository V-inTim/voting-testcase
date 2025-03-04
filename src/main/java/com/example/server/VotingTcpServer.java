package com.example.server;


import com.example.client.handler.TcpClientHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.StandardCharsets;

public class VotingTcpServer {
    private final int port;

    public VotingTcpServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) {
                            ch.pipeline().addLast(
                                    // new LineBasedFrameDecoder(1024), // Разделение по строкам
                                    new StringDecoder(StandardCharsets.UTF_8),
                                    new StringEncoder(StandardCharsets.UTF_8),
                                    new TcpClientHandler()
                            );
                        }
                    });

            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("Server started on port " + port);
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void run() {
        try {
            new VotingTcpServer(8080).start();
        } catch (InterruptedException e) {
            System.out.println("При запуске сервера что-то пошло нет так");
        }
    }
}
