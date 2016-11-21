package com.zxcc.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

/**
 * Created by xuanzh.cc on 2016/9/8.
 */
public class StringUtils extends org.apache.commons.lang.StringUtils{
    private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);

    public static final String CHARSET_STR = "utf-8";

    public static final Charset DEFAULT_CHARSET = Charset.forName(CHARSET_STR);

    /**
     * 获得字符串的字节码
     * @param content
     * @param charSet
     * @return
     */
    public static byte[] getBytes(String content, Charset charSet){
        if(isEmpty(content)) {
            String message = "字符串内容为空，无法获得字节码！";
            logger.error(message);
            throw new NullPointerException(message);
        }
        return content.getBytes(charSet);
    }

    /**
     * 获得字符串的字节码
     * @param content
     * @return
     */
    public static byte[] getBytes(String content){
        if(isEmpty(content)) {
            String message = "字符串内容为空，无法获得字节码！";
            logger.error(message);
            throw new NullPointerException(message);
        }
        return content.getBytes(DEFAULT_CHARSET);
    }

    /**
     * 判断是否为空串， 全部为空格也算空串
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断是否为非空串
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * url decode
     * @param str
     * @return
     */
    public static String URLDecode(String str) {
        try {
            return URLDecoder.decode(str, CHARSET_STR);
        } catch (UnsupportedEncodingException e) {
            logger.error("decode 失败,字符串为：{}", str, e );
        }
        return str;
    }
}
