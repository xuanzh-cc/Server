package com.zxcc.blog.search.service.impl;

import com.zxcc.blog.article.dto.ArticleDtoDetail;
import com.zxcc.blog.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by xuanzh.cc on 2016/9/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class SearchServiceImplTest {

    @Autowired
    private SearchService searchService;

    @Test
    public void search() throws Exception {
        List<ArticleDtoDetail> result = searchService.search("这是");
        System.out.println(result.size());
        System.out.println(result.get(0).getContent());
    }

}