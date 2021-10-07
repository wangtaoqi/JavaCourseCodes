package io.github.kimmking.gateway.inbound.okhttp;

import io.github.kimmking.gateway.filter.HttpRequestFilter;
import io.github.kimmking.gateway.filter.okhttp.OkHttpRequestExtFilter;
import io.github.kimmking.gateway.filter.okhttp.OkHttpRequestSignFilter;
import io.github.kimmking.gateway.inbound.HttpInboundHandlerSupport;
import io.github.kimmking.gateway.outbound.httpclient4.HttpOutboundHandler;
import io.github.kimmking.gateway.outbound.okhttp.OkhttpOutboundHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author wangnan
 * @create 10/7/2021
 */
public class OkHttpInBoundHandler extends HttpInboundHandlerSupport {

    private OkHttpInBoundHandler(HttpOutboundHandler outboundHandler, List<HttpRequestFilter> requestFilterList) {
        super(outboundHandler, requestFilterList);
    }

    public static OkHttpInBoundHandler createNewInstance(List<String> backendUrls){
        HttpOutboundHandler outboundHandler=new OkhttpOutboundHandler(backendUrls);
        List<HttpRequestFilter> requestFilterList= Arrays.asList(new OkHttpRequestSignFilter(),new OkHttpRequestExtFilter());
        return new OkHttpInBoundHandler(outboundHandler, requestFilterList);
    }
}
