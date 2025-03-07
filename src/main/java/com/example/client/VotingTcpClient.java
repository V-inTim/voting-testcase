package com.example.client;

import com.example.client.handler.TcpClientHandler;
import com.example.dto.Message;
import com.example.dto.UsernameMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class VotingTcpClient {
    private final String host;
    private final int port;
    private Channel channel;
    private String username;

    private final ObjectMapper objectMapper = new ObjectMapper();;

    public VotingTcpClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start(String username) throws InterruptedException {
        this.username = username;

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(
                                    new StringDecoder(StandardCharsets.UTF_8),
                                    new StringEncoder(StandardCharsets.UTF_8),
                                    new TcpClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect(host, port).sync();
            System.out.println("клиент запущен");

            channel = future.channel();

            Message message = new UsernameMessage(username);
            channel.writeAndFlush(objectMapper.writeValueAsString(message));

            future.channel().closeFuture().sync();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } finally {
            group.shutdownGracefully();
        }

    }

    public void sendMessage(Message message){
        String jsonMessage = null;
        try {
            jsonMessage = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        channel.writeAndFlush(jsonMessage);
    }

    public CompletableFuture<String> sendMessageAsync(Message message) {
        CompletableFuture<String> future = new CompletableFuture<>();
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            channel.writeAndFlush(jsonMessage).addListener(f -> {
                if (!f.isSuccess()) {
                    future.completeExceptionally(f.cause());
                }
            });

            TcpClientHandler.setResponseCallback(future::complete);
            System.out.println("установлено");// Устанавливаем обработчик ответа
        } catch (JsonProcessingException e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    public void disconnect(){
        channel.close();
    }

    public boolean checkLogin(){
        return username != null;
    }
}
