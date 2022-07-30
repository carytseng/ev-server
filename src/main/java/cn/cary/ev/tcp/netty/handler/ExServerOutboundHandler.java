package cn.cary.ev.tcp.netty.handler;

import cn.cary.ev.tcp.netty.message.ProtocolMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExServerOutboundHandler extends ChannelOutboundHandlerAdapter {


	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		ProtocolMessage out = ( ProtocolMessage) msg;
		ByteBuf bytes = Unpooled.copiedBuffer(out.toBytes());
		super.write(ctx, bytes, promise);
	}

	@Override
	public void flush(ChannelHandlerContext ctx) throws Exception {
		super.flush(ctx);
	}

}
