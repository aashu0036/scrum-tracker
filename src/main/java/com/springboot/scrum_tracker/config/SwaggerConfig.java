package com.springboot.scrum_tracker.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI swaggerCustomConfig() {
		
		
		OpenAPI openAPI= new OpenAPI().info(
			new Info()
			.title("Scrum Tracker APIs")
			.description("API description for User and Admin roles in Scrum tracker")
			).components(new Components());
		
		return openAPI;
	}
}
