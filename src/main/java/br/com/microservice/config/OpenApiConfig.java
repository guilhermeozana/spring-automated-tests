package br.com.microservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenApi(){
        return new OpenAPI()
                    .info(new Info()
                            .title("Hello Swagger Open API")
                            .version("v1")
                            .description("Spring Tests Course API")
                            .termsOfService("test")
                            .license( new License()
                                        .name("Apache 2.0")
                                        .url("test")));
    }
}
