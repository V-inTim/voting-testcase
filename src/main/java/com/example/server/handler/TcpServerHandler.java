package com.example.server.handler;

import com.example.dto.Message;
import com.example.dto.UsernameMessage;
import com.example.server.RequestProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TcpServerHandler extends SimpleChannelInboundHandler<String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger logger = LogManager.getLogger(TcpServerHandler.class);

    private final RequestProcessor requestProcessor;

    public TcpServerHandler() {
        this.requestProcessor = new RequestProcessor();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        logger.debug("Client message: {}", msg);

        Message message;
        try {
            message = objectMapper.readValue(msg, Message.class);
        } catch (JsonProcessingException e) {
            logger.error("Deserialization error: {}", e.getMessage());
            return;
        }

        if (!checkUsername(message, channelHandlerContext.channel())){
            Message response = requestProcessor.process(message);
            String jsonResponse = objectMapper.writeValueAsString(response);
            channelHandlerContext.writeAndFlush(jsonResponse); // Отправляем ответ
        }
    }

    private boolean checkUsername(Message message, Channel channel){
        if (!message.getType().equals("username"))
            return false;
        requestProcessor.setUsername(((UsernameMessage) message).getUsername());
        logger.info("username set");
        return true;
    }
}
