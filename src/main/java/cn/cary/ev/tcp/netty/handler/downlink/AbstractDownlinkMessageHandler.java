package cn.cary.ev.tcp.netty.handler.downlink;

import cn.cary.ev.tcp.netty.annotation.DownlinkMessageHandler;
import cn.cary.ev.tcp.session.Session;
import cn.cary.ev.tcp.session.SessionHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Map;

@Slf4j
@DownlinkMessageHandler
@Component
public abstract class AbstractDownlinkMessageHandler implements DownlinkMessageHandlerI {

    @Value("${facelink.singleOverTime}")
    public Long overTime;

    @Override
    public String getCommand() {
        return String.format(format, getClassFiy(), getOrder());
    }

    public void handleRequest(int deviceid, Map datas) throws Exception {

        String userName = (String) datas.get("name");
        Integer userId = (Integer) datas.get("id");
        Session session = SessionHelper.getSession(deviceid);
        if (session == null) {
            throw new RuntimeException("该设备不在线deviceId:" + deviceid);
        }

        synchronized (session) {
            /**多线程执行过程获取到锁，但session已经被覆盖的情况*/
            session = SessionHelper.getSession(deviceid);
            if (session == null) {
                throw new RuntimeException("该设备不在线deviceId:" + deviceid);
            }

        }

    }

}
