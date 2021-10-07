package io.github.kimmking.gateway.outbound.okhttp;

import io.github.kimmking.gateway.filter.okhttp.OkHttpResponseAddQueryUrlFilter;
import io.github.kimmking.gateway.filter.okhttp.OkHttpResponseAddTimeFilter;
import io.github.kimmking.gateway.filter.okhttp.OkHttpResponseSignUrlFilter;
import io.github.kimmking.gateway.outbound.HttpContext;
import io.github.kimmking.gateway.outbound.AbstractOutboundHandler;
import io.netty.handler.codec.rtsp.RtspHeaderNames;
import okhttp3.Call;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OkhttpOutboundHandler extends AbstractOutboundHandler<Request,Response> {

    private final OkHttpClient client;

    public OkhttpOutboundHandler(List<String> backendUrls) {
        super(backendUrls,  Arrays.asList(
                new OkHttpResponseSignUrlFilter(),
                new OkHttpResponseAddTimeFilter(),
                new OkHttpResponseSignUrlFilter(),
                new OkHttpResponseAddQueryUrlFilter()
                ));
        ConnectionPool connectionPool = new ConnectionPool(240,10,TimeUnit.SECONDS);
        this.client = new OkHttpClient.Builder().connectionPool(connectionPool).build();
    }

    private HttpContext<Request,Response>  handler(String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = client.newCall(request);
        final Response response = call.execute();
        return new HttpContext<Request, Response>(request, response);
    }

    private String end(String url) throws IOException {
//        String url = "http://127.0.0.1:8808";
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = client.newCall(request);
        final Response execute = call.execute();
        final ResponseBody body = execute.body();
        final byte[] bytes = body.bytes();
        String bodyContent = new String(bytes,"utf-8");
        return bodyContent;
    }

    @Override
    public byte[] getBody(HttpContext<Request, Response> httpContext) {
        final Response response = httpContext.getResponse();
        final ResponseBody body = response.body();
        try {
            return body.bytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public String getHeaderContentLength(HttpContext<Request, Response> httpContext) {
        final Response response = httpContext.getResponse();
        return response.header(RtspHeaderNames.CONTENT_LENGTH.toString(),"0");
    }

    @Override
    public String getHeaderContentType(HttpContext<Request, Response> httpContext) {
        final Response response = httpContext.getResponse();
        return response.header(RtspHeaderNames.CONTENT_TYPE.toString(),"");
    }

    @Override
    public String getUrl(HttpContext<Request, Response> httpContext) {
        final Request request = httpContext.getReuqest();
        return request.url().toString();
    }

    @Override
    public HttpContext<Request, Response> send(String url) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        final Call call = client.newCall(request);
        final Response response;
        try {
            response = call.execute();
            return new HttpContext<Request, Response>(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
