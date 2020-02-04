package com.sprainkle.gateway.handler;

import com.sprainkle.spring.cloud.advance.common.core.pojo.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * 从网关访问内部接口一律提示页面不存在
 *
 * <p>注意：所有内部接口的internal需放在第一或第二个path中</p>
 *
 * @author sprainkle
 * @date 2019-09-11
 */
@Component
public class InternalServiceHandler implements HandlerFunction<ServerResponse> {

	/**
	 * 内部接口
	 * @param request
	 * @return
	 */
	@Override
	public Mono<ServerResponse> handle(ServerRequest request) {

		return ServerResponse
			.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.body(BodyInserters.fromObject(new ErrorResponse(404, "Page Not Found")));
	}
}
