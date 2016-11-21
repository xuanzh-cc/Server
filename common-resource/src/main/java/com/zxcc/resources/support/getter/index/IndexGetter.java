package com.zxcc.resources.support.getter.index;

import com.zxcc.resources.support.getter.id.IdGetter;

import java.util.Comparator;

/**
 * 索引值获取接口
 * Created by xuanzh.cc on 2016/6/29.
 */
public interface IndexGetter extends IdGetter{

    /**
     * 获取索引名称
     * @return
     */
    String getIndexName();

    /**
     * 索引是否唯一
     * @return
     */
    boolean isUnique();

    /**
     * 是否存在索引排序器
     * @return
     */
    boolean hasComparator();

    /**
     * 获取索引排序器
     * @return
     */
    Comparator getComparator();
}
