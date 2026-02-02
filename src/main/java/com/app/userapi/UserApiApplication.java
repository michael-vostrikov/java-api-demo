package com.app.userapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.annotation.Import;
import com.app.common.system.JsonNodeJacksonComponent;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"com.app.userapi", "com.app.common"})
@EntityScan(basePackages = {"com.app.common"})
@EnableJpaRepositories("com.app.common")
@Import({JsonNodeJacksonComponent.class})
public class UserApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApiApplication.class, args);
	}

}
