package io.github.kimmking.gateway.inbound.okhttp;

import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.github.kimmking.gateway.inbound.HttpInboundInitializerDefault;
import io.github.kimmking.gateway.outbound.okhttp.OkhttpOutboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * @author wangnan
 * @create 10/7/2021
 */
public class OkHttpInboundInitializer extends HttpInboundInitializerDefault {

    private  OkHttpInboundInitializer(List<String> proxyServer, ChannelInboundHandlerAdapter handlerAdapter) {
        super(proxyServer, handlerAdapter);
    }

    public static OkHttpInboundInitializer createNewInstance(List<String> proxyServer){
        OkHttpInBoundHandler okHttpInBoundHandler = OkHttpInBoundHandler.createNewInstance(proxyServer);
        OkHttpInboundInitializer inboundInitializer = new OkHttpInboundInitializer(proxyServer,okHttpInBoundHandler);
        return inboundInitializer;
    }
}
