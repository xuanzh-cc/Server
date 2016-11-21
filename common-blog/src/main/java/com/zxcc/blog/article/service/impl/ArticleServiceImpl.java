package com.zxcc.blog.article.service.impl;

import com.zxcc.blog.article.constant.ArticleOperationMessage;
import com.zxcc.blog.article.dao.ArticleDao;
import com.zxcc.blog.article.dto.ArticleDtoBrief;
import com.zxcc.blog.article.dto.ArticleDtoDetail;
import com.zxcc.blog.article.entity.Article;
import com.zxcc.blog.article.exception.ArticleNotExistException;
import com.zxcc.blog.article.service.ArticleService;
import com.zxcc.blog.base.dto.PageInfo;
import com.zxcc.blog.category.constant.CategoryOperationMessage;
import com.zxcc.blog.category.exception.CategoryNotExistException;
import com.zxcc.blog.category.service.CategoryService;
import com.zxcc.blog.search.service.SearchService;
import com.zxcc.blog.tag.dao.TagDao;
import com.zxcc.blog.tag.exception.TagNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xuanzh.cc on 2016/9/8.
 */
@Component
public class ArticleServiceImpl implements ArticleService{

    /** 全部文章的数目 */
    private AtomicInteger articleNum = new AtomicInteger();

    /** 默认每页显示的数量 */
    @Value("${web.pageSize}")
    private int pageSize = 5;
    /** 页面上最多显示的分页数目 */
    @Value("${web.showPageNum}")
    private int showPageNum;

    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private TagDao tagDao;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SearchService searchService;

    @PostConstruct
    private void init(){
        int totalNum = this.articleDao.getTotalNum();
        this.articleNum.set(totalNum);
        PageInfo.setShowPageNum(this.showPageNum);
    }

    @Override
    public int getTotalNum() {
        return this.articleNum.get();
    }

    @Override
    public int getTotalPageNum(int pageSize) {
        if(pageSize <= 0) {
            pageSize = this.pageSize;
        }
        int pageNum = (int) Math.ceil(this.getTotalNum() / ( pageSize * 1.0D ));
        return pageNum;
    }

    @Override
    @Transactional
    public int addArticle(Article article) throws CategoryNotExistException, TagNotExistException {

        //类别不存在
        if(!this.categoryService.existCategory(article.getUser().getUserId(), article.getCategory().getCategoryId())){
            throw new CategoryNotExistException(CategoryOperationMessage.CATEGORY_NOT_EXIST);
        }

        //TODO 判断标签是否存在

        int result = articleDao.insertArticle(article);
        if(result > 0) {
            result = tagDao.insertArticleTags(article.getArticleId(), article.getTags());

            //文章总数 +1
            this.articleNum.incrementAndGet();

            //添加索引
            this.searchService.addIndex(article);


        }
        //这里要创建索引信息
        return result;
    }

    @Override
    public List<ArticleDtoBrief> getArticleBriefList(int offset, int limit) {
        List<ArticleDtoBrief> result = this.articleDao.queryAllBrief(offset, limit);
        return result;
    }

    @Override
    public ArticleDtoDetail getArticleDetailById(long articleId) {
        ArticleDtoDetail article = this.articleDao.queryDetailById(articleId);
        return article;
    }

    @Override
    @Transactional
    public ArticleDtoDetail viewArticle(long articleId){
        ArticleDtoDetail article = this.articleDao.queryDetailById(articleId);
        if(article != null){
            this.articleDao.incrClickHit(articleId);
        }
        return article;
    }

    @Override
    public boolean existArticle(long userId, long articleId) {
        int count = this.articleDao.existId(userId, articleId);
        return count >= 1;
    }

    @Override
    @Transactional
    public int updateArticle(Article article) throws ArticleNotExistException, CategoryNotExistException, TagNotExistException{

        //文章不存在
        if(!this.existArticle(article.getUser().getUserId(), article.getArticleId())) {
            throw new ArticleNotExistException(ArticleOperationMessage.ARTICLE_NOT_EXIST);
        }

        //类别不存在
        if(!this.categoryService.existCategory(article.getUser().getUserId(), article.getCategory().getCategoryId())){
            throw new CategoryNotExistException(CategoryOperationMessage.CATEGORY_NOT_EXIST);
        }

        //TODO 判断标签是否存在

        int result =  this.articleDao.updateArticle(article);
        if(result > 0 ) {
            long articleId = article.getArticleId();
            //删除标签信息
            this.tagDao.deleteAllTagIdByArticleId(articleId);
            //重新添加标签信息
            result = tagDao.insertArticleTags(articleId, article.getTags());

            //更新索引
            this.searchService.updateIndex(article);
        }
        return result;
    }

    @Override
    public int deleteById(long userId, long articleId) {
        //文章存在
        if(this.existArticle(userId, articleId)){
            //1、 删除文章对应的 标签
            this.tagDao.deleteAllTagIdByArticleId(articleId);
            //删除文章
            int result = this.articleDao.deleteById(articleId);

            //文章总数 -1
            this.articleNum.decrementAndGet();

            //删除索引
            this.searchService.deleteIndex(articleId);
            return result;
        }
        return 0;
    }

    @Override
    @Transactional
    public int deleteByArticleIdList(List<Long> articleIds) {
        //删除文章的全部标签
        this.tagDao.deleteAllTagIdByArticleIdList(articleIds);

        int result = this.articleDao.deleteByArticleIdList(articleIds);
        //文章总数减少 result
        this.articleNum.addAndGet(result);
        return result;
    }

    @Override
    public PageInfo getPageInfo(int currentPageNum, int pageSize) {
        int totalNum = this.getTotalNum();
        pageSize = pageSize < this.pageSize ? this.pageSize : pageSize;
        int totalPageNum = this.getTotalPageNum(pageSize);
        currentPageNum = currentPageNum < 1 ? 1 : currentPageNum;
        PageInfo pageInfo = PageInfo.valueOf(totalNum, totalPageNum, currentPageNum, pageSize);
        return pageInfo;
    }

    @Override
    public List<ArticleDtoBrief> getArticleByCategoryId(long categoryId) {
        List<ArticleDtoBrief> result = this.articleDao.queryByCategoryId(categoryId);
        return result;
    }
}
