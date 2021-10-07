package io.github.kimmking.gateway.outbound;

import io.github.kimmking.gateway.filter.HttpResponseFilter;
import io.github.kimmking.gateway.outbound.httpclient4.HttpOutboundHandler;
import io.github.kimmking.gateway.router.HttpEndpointRouter;
import io.github.kimmking.gateway.router.RandomHttpEndpointRouter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author wangnan
 * @create 10/7/2021
 */
public abstract class AbstractOutboundHandler<R, P> implements HttpOutboundHandler {
    protected List<String> backendUrls;
    protected HttpEndpointRouter router = new RandomHttpEndpointRouter();
    protected List<HttpResponseFilter<R, P>> httpResponseFilterList;

    public AbstractOutboundHandler(List<String> backendUrls, List<HttpResponseFilter<R, P>> httpResponseFilterList) {
        this.backendUrls = backendUrls.stream().map(this::formatUrl).collect(Collectors.toList());
        this.httpResponseFilterList = httpResponseFilterList;
    }

    protected String getBackendUrl() {
        return router.route(this.backendUrls);
    }

    protected String formatUrl(String backend) {
        return backend.endsWith("/") ? backend.substring(0, backend.length() - 1) : backend;
    }

    public abstract byte[] getBody(HttpContext<R, P> httpContext);

    public abstract String getHeaderContentLength(HttpContext<R, P> httpContext);

    public abstract String getHeaderContentType(HttpContext<R, P> httpContext);

    public abstract String getUrl(HttpContext<R, P> httpContext);
    public abstract  HttpContext<R, P> send(String url);
    private void handleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final HttpContext<R, P> httpContext) throws Exception {
        FullHttpResponse response = null;
        try {
            byte[] body = getBody(httpContext);
            String text =">提供服务的地址是:".concat(getUrl(httpContext));
//             int contentLength = Integer.parseInt(getHeaderContentLength(httpContext));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(body.length);
            outputStream.write(body,0,body.length);
            outputStream.write(text.getBytes(StandardCharsets.UTF_8));
           int contentLength = outputStream.size();
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(outputStream.toByteArray()));
            final FullHttpResponse fullHttpResponseFinal = response;
            final String contextType = getHeaderContentType(httpContext);
            if (contextType != null && !contextType.isEmpty()) {
                response.headers().set("Content-Type", contextType);
            } else {
                response.headers().set("Content-Type", "application/json");
            }
            response.headers().setInt("Content-Length", contentLength);
            if (httpResponseFilterList != null) {
                httpResponseFilterList.forEach(filter -> filter.filter(fullHttpResponseFinal, httpContext));
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    //response.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.write(response);
                }
            }
            ctx.flush();
            //ctx.close();
        }

    }

    @Override
    public void handle(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        final String backendUrl = getBackendUrl();
        String requestUrl =backendUrl+ fullRequest.uri();
        final HttpContext<R, P> httpContext = send(requestUrl);
        try {
            handleResponse(fullRequest,ctx,httpContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
