package cn.cary.ev.tcp.netty.handler.uplink;

import cn.cary.ev.tcp.netty.annotation.UplinkMessageHandler;
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
public class UplinkMessageHandlerRegistry implements ApplicationContextAware {

    private final static Map<String, UplinkMessageHandlerI> mapping = new HashMap<String, UplinkMessageHandlerI>();

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @PostConstruct
    public void init() throws Exception {
        Map<String, Object> handlers = applicationContext.getBeansWithAnnotation(UplinkMessageHandler.class);
        if (handlers == null) {
            return;
        }
        for (Object handler : handlers.values()) {
            if (!(handler instanceof UplinkMessageHandlerI)) {
                continue;
            }

            UplinkMessageHandlerI messageHandler = (UplinkMessageHandlerI) handler;
            mapping.put(messageHandler.getCommand(), messageHandler);
            log.info("UplinkMessageHandler {} command:{} registered", messageHandler,messageHandler.getCommand());
        }
    }

    public static UplinkMessageHandlerI getMessageHandler(String command) {
        UplinkMessageHandlerI handler = mapping.get(command);
        return handler;
    }
}
