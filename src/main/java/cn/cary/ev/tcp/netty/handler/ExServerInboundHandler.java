/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package cn.cary.ev.tcp.netty.handler;

import cn.cary.ev.tcp.netty.handler.uplink.UplinkMessageHandlerI;
import cn.cary.ev.tcp.netty.handler.uplink.UplinkMessageHandlerRegistry;
import cn.cary.ev.tcp.netty.message.ProtocolMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Handles both client-side and server-side handler depending on which
 * constructor was called.
 */
@Slf4j
@Component
public class ExServerInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("Server channelRegistered: " + ctx.channel().remoteAddress().toString());
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("Server channelUnregistered: " + ctx.channel().remoteAddress().toString());
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Server channelActive: " + ctx.channel().remoteAddress().toString());
        super.channelActive(ctx);

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("Server channelInactive: " + ctx.channel().remoteAddress().toString());
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
        ProtocolMessage var = (ProtocolMessage) msg;
        UplinkMessageHandlerI handlerI = UplinkMessageHandlerRegistry.getMessageHandler(String.valueOf(var.getClassfiy()+var.getOrder()));
        handlerI.handleRequest(ctx, var);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("Server userEventTriggered: " + ctx.channel().remoteAddress().toString());
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        log.info("Server channelWritabilityChanged: " + ctx.channel().remoteAddress().toString());
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("Server exceptionCaught: " + ctx.channel().remoteAddress().toString());
        super.exceptionCaught(ctx, cause);
    }

}