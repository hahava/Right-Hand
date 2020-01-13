package com.righthand;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


@EnableRedisHttpSession
@EnableJpaAuditing
@SpringBootApplication
@EnableCaching
public class RighthandApplication {

	private static final String PROPERTIES = "spring.config.location=" +
			"classpath:/application.yml" +
			",classpath:/db.yml";

	public static void main(String[] args) {

		new SpringApplicationBuilder(RighthandApplication.class)
				.properties(PROPERTIES)
				.build();
	}
}

