package io.github.kimmking.gateway.inbound;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.util.List;

public class HttpInboundInitializerDefault extends ChannelInitializer<SocketChannel> {

	private List<String> proxyServer;
	private ChannelInboundHandlerAdapter handlerAdapter;
	public HttpInboundInitializerDefault(List<String> proxyServer,ChannelInboundHandlerAdapter handlerAdapter) {
		this.proxyServer = proxyServer;
		this.handlerAdapter = handlerAdapter;
	}

	@Override
	public void initChannel(SocketChannel ch) {
		ChannelPipeline p = ch.pipeline();
//		if (sslCtx != null) {
//			p.addLast(sslCtx.newHandler(ch.alloc()));
//		}
		p.addLast(new HttpServerCodec());
		//p.addLast(new HttpServerExpectContinueHandler());
		p.addLast(new HttpObjectAggregator(1024 * 1024));
		if (handlerAdapter != null) {
			p.addLast(handlerAdapter);
		}
	}
}
