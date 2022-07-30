package cn.cary.ev.tcp.netty.codec.decode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteOrder;

@Slf4j
public class CustomCoderByteToMessageDecoder extends LengthFieldBasedFrameDecoder {

    private static final int HEADER_SIZE = 8;

    public CustomCoderByteToMessageDecoder(
            ByteOrder byteOrder,
            int maxFrameLength,
            int lengthFieldOffset,
            int lengthFieldLength,
            int lengthAdjustment,
            int initialBytesToStrip,
            boolean failFast) {
        super(byteOrder, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip,
                failFast);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        //在这里调用父类的方法,实现指得到想要的部分,我在这里全部都要,也可以只要body部分
        in = (ByteBuf) super.decode(ctx, in);
        if (in == null) {
            return null;
        }

        int readerIndex = in.readerIndex();
        if (in.readableBytes() < HEADER_SIZE) {
            throw new Exception("字节数不足 HEADER_SIZE 8");
        }
        //id
        Integer id = in.readInt();
        //长度
        Integer length = in.readInt();

        in.readerIndex(readerIndex);

        //读取body
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        return bytes;
    }

}
