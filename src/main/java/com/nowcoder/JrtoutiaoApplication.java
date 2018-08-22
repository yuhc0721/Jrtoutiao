package com.nowcoder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class JrtoutiaoApplication extends SpringBootServletInitializer {



	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(JrtoutiaoApplication.class);
	}
	public static void main(String[] args) {
		SpringApplication.run(JrtoutiaoApplication.class, args);
	}
}
