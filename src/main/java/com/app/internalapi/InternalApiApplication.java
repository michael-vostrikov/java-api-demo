package com.app.internalapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.Import;
import com.app.common.system.JsonNodeJacksonComponent;

@SpringBootApplication
@ComponentScan(basePackages = {"com.app.internalapi", "com.app.common"})
@EntityScan(basePackages = {"com.app.common"})
@EnableJpaRepositories("com.app.common")
@Import({JsonNodeJacksonComponent.class})
public class InternalApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(InternalApiApplication.class, args);
	}

}
