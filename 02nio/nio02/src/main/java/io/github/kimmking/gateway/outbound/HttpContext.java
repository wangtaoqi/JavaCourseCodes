package io.github.kimmking.gateway.outbound;

import io.netty.handler.codec.http.FullHttpResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.http.HttpResponse;

/**
 * @author wangnan
 * @create 10/7/2021
 */
@Data
@AllArgsConstructor
public class HttpContext<R,P> {
    private R reuqest;
    private P response;
}
