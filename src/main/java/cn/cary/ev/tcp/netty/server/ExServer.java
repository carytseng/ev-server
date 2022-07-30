/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package cn.cary.ev.tcp.netty.server;


import cn.cary.ev.tcp.netty.codec.decode.CustomCoderByteToMessageDecoder;
import cn.cary.ev.tcp.netty.codec.decode.CustomProtocolParams;
import cn.cary.ev.tcp.netty.codec.encode.CustomCoderByteArrayEncoder;
import cn.cary.ev.tcp.netty.handler.ExServerInboundHandler;
import cn.cary.ev.tcp.netty.handler.ExServerOutboundHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.nio.ByteOrder;

/**
 * Netty server config
 *
 * @author 郑剑锋
 * @date 2022/7/29 4:23 PM
 */
@Component("ExServer")
@Slf4j
public class ExServer implements CommandLineRunner {

    static final boolean SSL = System.getProperty("ssl") != null;

    @Value("${tcp.ip:127.0.0.1}")
    private String ip;
    @Value("${tcp.port:10080}")
    private Integer port;

    public void startService(String address, Integer port) {
        try {
            // Configure SSL.
            final SslContext sslCtx;
            if (SSL) {
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            } else {
                sslCtx = null;
            }

            EventLoopGroup bossGroup = new NioEventLoopGroup(1);
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler(LogLevel.INFO))
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            public void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline p = ch.pipeline();
                                if (sslCtx != null) {
                                    p.addLast(sslCtx.newHandler(ch.alloc()));
                                }
                                p.addLast(
                                        new CustomCoderByteArrayEncoder(),
                                        new CustomCoderByteToMessageDecoder(ByteOrder.BIG_ENDIAN,
                                                CustomProtocolParams.maxFrameLength,
                                                CustomProtocolParams.lengthFieldOffset, CustomProtocolParams.lengthFieldLength, CustomProtocolParams.lengthAdjustment,
                                                CustomProtocolParams.initialBytesToStrip,
                                                CustomProtocolParams.failFast)
                                );

                                p.addLast(new ExServerOutboundHandler());
                                p.addLast(new ExServerInboundHandler());
                            }
                        });

                // Bind and start to accept incoming connections.
                log.info("DEVICE SERVER SUCCESS " + port);
                InetAddress inetHost = InetAddress.getByName(address);
                b.bind(inetHost, port).sync().channel().closeFuture().sync();
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        } catch (Exception e) {
            log.error("设备伺服线程启动失败:address:{} port:{} message:{}", address, port, e.getMessage());
        }
    }

    @Override
    public void run(String... args) throws Exception {
        this.startService(ip, port);
    }
}