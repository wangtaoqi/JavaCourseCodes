package io.github.kimmking.gateway.filter;

import io.github.kimmking.gateway.outbound.HttpContext;
import io.netty.handler.codec.http.FullHttpResponse;

/**
 * R 表示 request  P表示 response
 * @param <R>
 * @param <P>
 */
public interface HttpResponseFilter<R,P> {

    void filter(FullHttpResponse response, HttpContext<R, P> httpContext);

}
