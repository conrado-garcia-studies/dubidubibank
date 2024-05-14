package br.dubidubibank;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@OpenAPIDefinition(info = @Info(title = "Dubidubibank"))
@SpringBootApplication
public class DubidubibankApplication {
  public static void main(String[] args) {
    SpringApplication.run(DubidubibankApplication.class, args);
  }
}
