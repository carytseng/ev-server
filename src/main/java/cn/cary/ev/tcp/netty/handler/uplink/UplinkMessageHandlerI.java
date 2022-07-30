package cn.cary.ev.tcp.netty.handler.uplink;

import cn.cary.ev.tcp.netty.handler.MessageHandlerI;
import cn.cary.ev.tcp.netty.message.ProtocolMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @program: ib-common-server
 * @description: 协议上行消息处理器
 * @author: 郑剑锋
 * @create: 2021-03-04 11:30
 **/
public interface UplinkMessageHandlerI extends MessageHandlerI {

    /**
     * @描述:消息处理
     */
    void handleRequest(ChannelHandlerContext ctx, ProtocolMessage request) throws Exception;

    /**
     * @描述:响应消息构造
     */
    ProtocolMessage buildResponse(int deviceId,ProtocolMessage request,byte[] respData) throws Exception;

}
