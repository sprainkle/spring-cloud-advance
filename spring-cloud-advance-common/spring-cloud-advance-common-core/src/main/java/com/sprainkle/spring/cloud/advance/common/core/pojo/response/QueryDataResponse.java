package com.sprainkle.spring.cloud.advance.common.core.pojo.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>查询数据集返回结果</p>
 *
 * @author sprainkle
 * @date 2019/5/2
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryDataResponse<T> extends CommonResponse<QueryData<T>> {
    public QueryDataResponse() {
    }

    public QueryDataResponse(QueryData<T> data) {
        super(data);
    }
}
