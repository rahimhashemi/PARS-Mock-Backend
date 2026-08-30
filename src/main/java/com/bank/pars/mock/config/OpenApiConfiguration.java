package com.bank.pars.mock.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class OpenApiConfiguration {

    @Bean
    OpenAPI parsMockOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("PARS Mock Backend API")
                        .version("1.0.0")
                        .description(
                                "Development-only PARS JWT issuer used for Token Bridge integration testing. "
                                        + "Never use this mock as a production issuer."))
                .servers(List.of(
                        new Server()
                                .url("http://127.0.0.1:9080")
                                .description("Local PARS Mock")));
    }
}
