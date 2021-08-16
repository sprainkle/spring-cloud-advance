package com.sprainkle.spring.cloud.advance.common.lock.property;

import lombok.Data;

import java.util.List;

/**
 * <pre>
 *
 * </pre>
 *
 * @author sprainkle
 * @date 2019/04/20
 */
@Data
public class RedisProperties {

    private String host;
    private String password;
    private String port;
    private Sentinel sentinel;

	@Data
    public static class Sentinel {
		/**
		 * Name of the Redis server.
		 */
		private String master;

		/**
		 * Comma-separated list of "host:port" pairs.
		 */
		private List<String> nodes;
	}

}
