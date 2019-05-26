package com.sprainkle.spring.cloud.advance.common.core.pojo.param;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 分页对象
 * @author sprainkle
 * @date 2018.10.26
 */
public class QueryPage<T> extends Page<T> {
	private static final Long DEFAULT_PAGE_NO = 1L;
	private static final Long DEFAULT_PAGE_SIZE = 10L;

	public QueryPage() {
		super(DEFAULT_PAGE_NO, DEFAULT_PAGE_SIZE);
	}

	public QueryPage(QueryParam queryParam) {
		super(queryParam.getPageNo(), queryParam.getPageSize());
	}

	public Long getPageNo() {
		return this.getCurrent();
	}

	public void setPageNo(String pageNo) {
		if (StrUtil.isNotBlank(pageNo)) {
			this.setCurrent(Integer.parseInt(pageNo));
		}
	}

	public Long getPageSize() {
		return this.getSize();
	}

	public void setPageSize(String pageSize) {
		if (StrUtil.isNotBlank(pageSize)) {
			this.setSize(Integer.parseInt(pageSize));
		}
	}

}
