package cn.cary.ev.tcp.netty.handler.downlink;


import cn.cary.ev.tcp.netty.handler.MessageHandlerI;
import java.util.Map;

public interface DownlinkMessageHandlerI extends MessageHandlerI {

    /**下行消息接口*/
    void handleRequest(int deviceid, Map<String,Object> params) throws Exception;

}
