package cn.cary.ev.tcp.session;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Data
@Configuration
public class SessionHelper {

    /**
     * 单例
     */
    private static SessionHelper instance;

    public static SessionHelper getInstance() {
        return instance;
    }

    @Bean
    public SessionHelper init() {
        instance = new SessionHelper();
        sessions = new ConcurrentHashMap<>();
        return instance;
    }

    /**
     * 所有设备会话
     */
    private static Map<Integer, Session> sessions;

    /**
     * @描述: 注册设备
     */
    public void registerDevice(ChannelHandlerContext ctx, int deviceId) {
        Session session = getSession(deviceId);
        if (session == null) {
            session = new Session(ctx, System.currentTimeMillis());
            putSession(deviceId, session);
            log.info("设备注册 -> deviceId:{} 存放session:{}", deviceId, session.hashCode());
        }
        /**实时更新channel*/
        session.setContext(ctx);
        session.setLastTime(System.currentTimeMillis());
    }

    /**
     * @描述: 下线设备
     */
    public void deRegisterDevice(int deviceId) {
        Session session = getSession(deviceId);
        if (session != null) {
            session.getContext().channel().close();
            sessions.remove(deviceId);
            log.info("设备注销 -> deviceId:{}", deviceId);
        }
        return;
    }

    /**
     * @描述: 检查设备在线状态
     */
    public static boolean checkStatus(int deviceId) {
        Session session = getSession(deviceId);
        return session != null;
    }

    private static Session putSession(int deviceseqid, Session value) {
        return sessions.put(deviceseqid, value);
    }

    public static Session getSession(int deviceseqid) {
        return sessions.get(deviceseqid);
    }

    public static Map<Integer, Session> getSessions() {
        return sessions;
    }


}
