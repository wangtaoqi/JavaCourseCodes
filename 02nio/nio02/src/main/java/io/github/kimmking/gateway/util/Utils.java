package io.github.kimmking.gateway.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author wangnan
 * @create 10/6/2021
 */
public class Utils {
    public static String textDecode(String s) {
        try {
            final String decode = URLDecoder.decode(s, "utf-8");
            return decode;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
