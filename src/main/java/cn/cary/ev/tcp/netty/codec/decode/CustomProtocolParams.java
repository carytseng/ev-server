package cn.cary.ev.tcp.netty.codec.decode;

public interface CustomProtocolParams {
	int maxFrameLength = 1024;
	int lengthFieldOffset = 4;
	int lengthFieldLength = 4;
	int lengthAdjustment =-8;
    int  initialBytesToStrip=0;
    boolean failFast = true;
}
