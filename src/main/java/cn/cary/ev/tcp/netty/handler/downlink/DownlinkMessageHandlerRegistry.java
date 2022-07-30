package cn.cary.ev.tcp.netty.handler.downlink;

import cn.cary.ev.tcp.netty.annotation.DownlinkMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class DownlinkMessageHandlerRegistry implements ApplicationContextAware {

    private final static Map<String, DownlinkMessageHandlerI> mapping = new HashMap<String, DownlinkMessageHandlerI>();

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**容器启动初始化所有消息处理器*/
    @PostConstruct
    public void init() throws Exception {
        Map<String, Object> handlers = applicationContext.getBeansWithAnnotation(DownlinkMessageHandler.class);
        if (handlers == null) {
            return;
        }
        for (Object handler : handlers.values()) {
            if (!(handler instanceof DownlinkMessageHandlerI)) {
                continue;
            }

            DownlinkMessageHandlerI messageHandler = (DownlinkMessageHandlerI) handler;
            mapping.put(messageHandler.getCommand(), messageHandler);
            log.info("DownlinkMessageHandler {} command:{} registered", messageHandler,messageHandler.getCommand());
        }
    }

    public static DownlinkMessageHandlerI getMessageHandler(String command) {
        DownlinkMessageHandlerI handler = mapping.get(command);
        return handler;
    }

}
