package cn.cary.ev.tcp.session;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
public class Session {
	
	public Session(ChannelHandlerContext context , long createTime ) {
		this.context = context ;
		this.createTime = createTime;
	}

	private ChannelHandlerContext context ; 
	private long createTime = -1;
	private long lastTime 	= -1;
	private long writeTime 	= -1;
	private Map attr = new HashMap();
	private int status = SessionStatus.writerable ;

	public interface SessionStatus {
		/**
		 * 正在写
		 */
		int writering = 100;
		/**
		 * 写完成,等待设备回复响应
		 */
		int writered = 101;

		/**
		 * 可写状态
		 */
		int  writerable= 200;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Session session = (Session) o;
		return createTime == session.createTime && Objects.equals(context, session.context);
	}

	@Override
	public int hashCode() {
		return Objects.hash(context, createTime);
	}
}
