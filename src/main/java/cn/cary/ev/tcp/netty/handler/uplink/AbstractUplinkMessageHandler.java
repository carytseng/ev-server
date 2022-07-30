package cn.cary.ev.tcp.netty.handler.uplink;

public abstract class AbstractUplinkMessageHandler implements UplinkMessageHandlerI {

    @Override
    public String getCommand() {
        return String.format(format, getClassFiy(), getOrder());
    }


}
