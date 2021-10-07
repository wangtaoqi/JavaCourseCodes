package io.github.kimmking.gateway.inbound;

import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.github.kimmking.gateway.outbound.httpclient4.HttpOutboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class HttpInboundHandlerSupport extends ChannelInboundHandlerAdapter {

    protected  Logger logger = LoggerFactory.getLogger(this.getClass());
    private HttpOutboundHandler outboundHandler;
    private List<HttpRequestFilter> requestFilterList = new ArrayList<>();

    public HttpInboundHandlerSupport(HttpOutboundHandler outboundHandler,List<HttpRequestFilter> requestFilterList) {
        this.outboundHandler = outboundHandler;
        this.requestFilterList=requestFilterList;
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            if (requestFilterList != null) {
                requestFilterList.forEach(requestFilter->requestFilter.filter(fullRequest, ctx));
            }
            outboundHandler.handle(fullRequest, ctx);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
