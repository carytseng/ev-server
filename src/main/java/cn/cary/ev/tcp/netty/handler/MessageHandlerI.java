package cn.cary.ev.tcp.netty.handler;

public interface MessageHandlerI {

    String format = "%d-%d";

    /**
     * @描述:分类-命令
     */
    String getCommand();

    /**
     * @描述:分类
     */
    int getClassFiy();

    /**
     * @描述:命令
     */
    int getOrder();

}
