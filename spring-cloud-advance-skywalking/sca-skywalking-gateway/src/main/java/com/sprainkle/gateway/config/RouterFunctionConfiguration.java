package com.sprainkle.gateway.config;

import com.sprainkle.gateway.handler.InternalServiceHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

/**
 * 路由配置信息
 *
 * @author sprainkle
 * @date 2019-09-11
 */
@Configuration
public class RouterFunctionConfiguration {

    public RouterFunctionConfiguration(InternalServiceHandler internalServiceHandler) {
        this.internalServiceHandler = internalServiceHandler;
    }

    private final InternalServiceHandler internalServiceHandler;

	@Bean
	public RouterFunction routerFunction() {
		return RouterFunctions
			// 前置**无法匹配多个路径，如/**无法匹配/admin/user
			.route(RequestPredicates.path("/*/internal/**")
				.and(RequestPredicates.accept(MediaType.ALL)), internalServiceHandler)
			.andRoute(RequestPredicates.path("/*/*/internal/**")
				.and(RequestPredicates.accept(MediaType.ALL)), internalServiceHandler);

	}

}
