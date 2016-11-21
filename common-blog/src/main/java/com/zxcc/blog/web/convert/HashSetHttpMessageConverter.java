package com.zxcc.blog.web.convert;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by xuanzh.cc on 2016/8/22.
 */
public class HashSetHttpMessageConverter extends AbstractHttpMessageConverter {

    private static List<MediaType> supportsType = new ArrayList<MediaType>();

    static {
        supportsType.add(MediaType.valueOf("application/x-www-form-urlencoded;charset=UTF-8"));
    }

    @Override
    protected boolean supports(Class clazz) {
        return clazz.isAssignableFrom(Set.class);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return supportsType;
    }

    @Override
    protected Object readInternal(Class clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        String reqParamStr = StreamUtils.copyToString(inputMessage.getBody(), Charset.forName("UTF-8"));
        reqParamStr = URLDecoder.decode(reqParamStr, "UTF-8");
        System.out.println(reqParamStr);
        return null;
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

    }
}
