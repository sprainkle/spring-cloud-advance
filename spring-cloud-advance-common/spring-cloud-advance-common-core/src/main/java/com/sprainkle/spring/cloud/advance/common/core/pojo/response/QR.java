package com.sprainkle.spring.cloud.advance.common.core.pojo.response;

import lombok.ToString;

/**
 * 响应信息主体，同{@link QueryDataResponse}
 *
 * @param <T>
 * @author sprainkle
 * @date 2019/5/2
 * @see QueryDataResponse
 */
@ToString
public class QR<T> extends QueryDataResponse<T> {

	public QR() {
		super();
	}

	public QR(QueryData<T> data) {
		super(data);
	}
}
