package com.sprainkle.spring.cloud.advance.common.core.pojo.param;


import com.sprainkle.spring.cloud.advance.common.core.pojo.request.BaseRequest;

/**
 * <p>查询条件参数类</p>
 *
 * @author sprainkle
 * @date 2019/5/2
 */
public class QueryParam extends BaseRequest {
    /**
     * 默认页码，第一页
     */
    private static final int DEFAULT_PAGE_NO = 1;
    /**
     * 默认分页大小，默认10条记录
     */
    private static final int DEFAULT_PAGE_SIZE = 10;
    /**
     * 页码
     */
    protected Integer pageNo = DEFAULT_PAGE_NO;
    /**
     * 分页大小
     */
    protected Integer pageSize = DEFAULT_PAGE_SIZE;

    /**
     * 页码
     */
    public Integer getPageNo() {
        if (this.pageNo == null) {
            return DEFAULT_PAGE_NO;
        }

        return this.pageNo;
    }

    /**
     * 页码
     */
    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    /**
     * 分页大小
     */
    public Integer getPageSize() {
        if (this.pageSize == null) {
            return DEFAULT_PAGE_SIZE;
        }
        return this.pageSize;
    }

    /**
     * 分页大小
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

}
