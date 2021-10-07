package io.github.kimmking.gateway.filter.okhttp;

import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author wangnan
 * @create 10/7/2021
 */
public class OkHttpRequestSignFilter implements HttpRequestFilter {
    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        fullRequest.headers().set("filter-sign", "OkHttpRequestFilter");
    }
}
