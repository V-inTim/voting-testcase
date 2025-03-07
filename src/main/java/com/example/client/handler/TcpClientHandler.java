package com.example.client.handler;

import com.example.dto.Message;
import com.example.dto.ReplyMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Setter;

import java.util.function.Consumer;

public class TcpClientHandler extends SimpleChannelInboundHandler<String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Setter
    private static Consumer<String> responseCallback;


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        Message message;
        try {
            message = objectMapper.readValue(msg, Message.class);
        } catch (JsonProcessingException e) {
            System.out.println("Ошибка при десериализации сообщения");
            System.out.println(e.getMessage());
            return;
        }
        ReplyMessage replyMessage = (ReplyMessage) message;
        System.out.println(replyMessage.getResponse());
        if (responseCallback != null && replyMessage.getType().equals("preview response")) {
            responseCallback.accept(msg);
            responseCallback = null; // Сбрасываем после вызова
        }
    }

}
