package io.github.kimmking.gateway.filter.okhttp;

import io.github.kimmking.gateway.filter.HttpResponseFilter;
import io.github.kimmking.gateway.outbound.HttpContext;
import io.netty.handler.codec.http.FullHttpResponse;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author wangnan
 * @create 10/7/2021
 */
public class OkHttpResponseAddTimeFilter implements HttpResponseFilter<Request, Response> {
    @Override
    public void filter(FullHttpResponse response, HttpContext<Request,Response> httpContext) {
        response.headers().set("systemTime", String.valueOf(System.currentTimeMillis()));
    }
}
