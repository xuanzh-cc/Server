package com.zxcc.blog.search.service.impl;

import com.chenlb.mmseg4j.analysis.SimpleAnalyzer;
import com.zxcc.blog.article.dto.ArticleDtoDetail;
import com.zxcc.blog.article.entity.Article;
import com.zxcc.blog.search.service.SearchService;
import com.zxcc.utility.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuanzh.cc on 2016/9/13.
 */
@Component
public class SearchServiceImpl implements SearchService{

    private static final String ARTICLE_ID = "articleId";
    private static final String TITLE = "title";
    private static final String CONTENT = "content";

    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    private Directory directory;
    private Analyzer analyzer;
    private IndexWriterConfig indexWriterConfig;
    private IndexWriter indexWriter;
    private IndexReader indexReader;


    /** 索引目录 */
    @Value("${lucene.indexPath}")
    private String indexPath = "D:/lucene/index";

    /** 索引结果条数 */
    @Value("${lucene.searchNum}")
    private int searchNum = 30;

    @PostConstruct
    public void init(){
        try {
            logger.error("初始化索引开始...");
            this.directory = FSDirectory.open(Paths.get(this.indexPath));
            this.analyzer = new SimpleAnalyzer();
            this.indexWriterConfig = new IndexWriterConfig(this.analyzer);

            this.indexWriter = new IndexWriter(this.directory, this.indexWriterConfig);
            this.indexWriter.commit();
            this.indexReader = DirectoryReader.open(this.directory);
            logger.error("初始化索引结束...");
        } catch (IOException e) {
            logger.error("索引service 初始化失败，索引目录：{}", this.indexPath, e);
        }
    }

    @Override
    public void addIndex(Article article){
        Document document = new Document();
        document.add(new StringField(ARTICLE_ID, String.valueOf(article.getArticleId()), Field.Store.YES));
        document.add(new TextField(TITLE, article.getTitle(), Field.Store.YES));
        document.add(new TextField(CONTENT, article.getContent(), Field.Store.YES));
        try {
            indexWriter.addDocument(document);
            indexWriter.commit();
        } catch (IOException e) {
            logger.error("索引添加失败，文章id：{}", article.getArticleId(), e);
        }
    }

    @Override
    public void updateIndex(Article article) {
        Document document = new Document();
        document.add(new StringField(ARTICLE_ID, String.valueOf(article.getArticleId()), Field.Store.YES));
        document.add(new TextField(TITLE, article.getTitle(), Field.Store.YES));
        document.add(new TextField(CONTENT, article.getContent(), Field.Store.YES));

        Term term = new Term("articleId", String.valueOf(article.getArticleId()));

        try {
            //更新的时候会把原来的索引删除，重新生成一个索引
            this.indexWriter.updateDocument(term, document);
            this.indexWriter.commit();
        } catch (IOException e) {
            logger.error("索引更新失败，文章id：{}", article.getArticleId(), e);
        }
    }

    @Override
    public void deleteIndex(long articleId) {
        Term term = new Term("articleId", String.valueOf(articleId));
        try {
            this.indexWriter.deleteDocuments(term);
            this.indexWriter.commit();
        } catch (IOException e) {
            logger.error("索引删除失败，文章id：{}", articleId, e);
        }
    }

    @Override
    public List<ArticleDtoDetail> search(String keywords) {

        try {
            IndexSearcher indexSearcher = this.getIndexSearcher();

            //标题
            QueryParser titleQueryParser = new QueryParser(TITLE, this.analyzer);
            Query titleQuery = titleQueryParser.parse(keywords);

            //内容
            QueryParser contentQueryParser = new QueryParser(CONTENT, this.analyzer);
            Query contentQuery = contentQueryParser.parse(keywords);

            BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
            booleanQueryBuilder.add(titleQuery, BooleanClause.Occur.SHOULD);
            booleanQueryBuilder.add(contentQuery, BooleanClause.Occur.SHOULD);

            TopDocs topDocs = indexSearcher.search(booleanQueryBuilder.build(), this.searchNum);

            QueryScorer scorer=new QueryScorer(titleQuery);
            Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
            SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color='red'>","</font></b>");
            Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
            highlighter.setTextFragmenter(fragmenter);

            List<ArticleDtoDetail> result = new ArrayList<>();

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document document = indexSearcher.doc(scoreDoc.doc);
                ArticleDtoDetail article = new ArticleDtoDetail();
                article.setArticleId(Long.parseLong(document.get(ARTICLE_ID)));

                //标题高亮
                String title = document.get(TITLE);
                if(StringUtils.isNotEmpty(title)) {
                    TokenStream tokenStream = this.analyzer.tokenStream(TITLE, new StringReader(title));
                    String highlightTitle = highlighter.getBestFragment(tokenStream, title);
                    if(StringUtils.isNotEmpty(highlightTitle)) {
                        article.setTitle(highlightTitle);
                    } else {
                        article.setTitle(title);
                    }
                }

                //内容高亮
                String content = document.get(CONTENT);
                if(StringUtils.isNotEmpty(content)){
                    TokenStream tokenStream = this.analyzer.tokenStream(CONTENT, new StringReader(content));
                    String highlightContent = highlighter.getBestFragment(tokenStream, content);
                    if(StringUtils.isNotEmpty(highlightContent)) {
                        article.setContent(highlightContent);
                    } else {
                        article.setContent(content);
                    }
                }
                result.add(article);
            }

            return result;

        } catch (Exception e) {
            logger.error("搜索异常，搜索关键字：{}", keywords, e);
        }

        return new ArrayList<ArticleDtoDetail>();
    }

    /**
     * 获取 IndexSearcher
     * @return
     */
    private IndexSearcher getIndexSearcher(){
        try {
            IndexReader newIndexReader = DirectoryReader.openIfChanged((DirectoryReader) this.indexReader);
            if(newIndexReader != null) {
                this.indexReader.close();
                this.indexReader = newIndexReader;
            }
        } catch (IOException e) {
            logger.error("获取 indexReader 失败", e);
        }
        return new IndexSearcher(this.indexReader);
    }

    @PreDestroy
    public void destroy(){
        logger.error("初始关闭开始...");
        try {
            if(this.indexReader != null)
                this.indexReader.close();
            if(this.indexWriter != null)
                this.indexWriter.close();
            logger.error("初始关闭结束...");
        } catch (IOException e) {
            logger.error("索引关闭失败！", e);
        }
    }
}
