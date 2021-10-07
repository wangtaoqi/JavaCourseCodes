package io.github.kimmking.gateway.outbound.httpclient4;

import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author wangnan
 * @create 10/7/2021
 */
public interface HttpOutboundHandler {
    void handle(FullHttpRequest fullRequest, ChannelHandlerContext ctx);
}
