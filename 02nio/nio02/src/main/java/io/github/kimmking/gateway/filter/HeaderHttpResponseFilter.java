package io.github.kimmking.gateway.filter;

import io.github.kimmking.gateway.outbound.HttpContext;
import io.netty.handler.codec.http.FullHttpResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.net.MalformedURLException;

public class HeaderHttpResponseFilter implements HttpResponseFilter<HttpGet,HttpResponse> {
    @Override
    public void filter(FullHttpResponse response, HttpContext<HttpGet, HttpResponse> httpContext) {
        response.headers().set("kk", "java-1-nio");
        try {
            response.headers().set("request-url", httpContext.getReuqest().getURI().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
