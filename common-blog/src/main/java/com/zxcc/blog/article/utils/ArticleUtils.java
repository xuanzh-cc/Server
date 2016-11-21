package com.zxcc.blog.article.utils;

/**
 * 文章工具类
 * Created by xuanzh.cc on 2016/9/10.
 */
public class ArticleUtils {
    private static final char filterChar = '\u200B';

    /**
     * 去掉 文章的html标签
     * @param content
     * @return
     */
    public static String getPlainTxt(String content) {
        content = content.replaceAll("[\\n\\r]", "");
        return content;
    }
}


/*

    var agent = navigator.userAgent.toLowerCase();
    var ie = /(msie\s|trident.*rv:)([\w.]+)/.test(agent);
    var fillChar = ie && browser.version == '6' ? '\ufeff' : '\u200B';

    var reg = new RegExp(fillChar, 'g');
    var html = contentHtml.replace(/[\n\r]/g, '');//ie要先去了\n在处理

    html = html.replace(/<(p|div)[^>]*>(<br\/?>|&nbsp;)<\/\1>/gi, '\n')
        .replace(/<br\/?>/gi, '\n')
        .replace(/<[^>/]+>/g, '')
        .replace(/(\n)?<\/([^>]+)>/g, function (a, b, c) {
            return dtd.$block[c] ? '\n' : b ? b : '';
        });
    //取出来的空格会有c2a0会变成乱码，处理这种情况\u00a0
    return html.replace(reg, '').replace(/\u00a0/g, ' ').replace(/&nbsp;/g, ' ');


 */