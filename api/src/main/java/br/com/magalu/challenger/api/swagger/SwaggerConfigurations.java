package br.com.magalu.challenger.api.swagger;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
class SwaggerConfigurations {

  @Value("${application.version}")
  private String version;

  @Value("${application.name}")
  private String name;

  @Value("${application.description}")
  private String description;

  @Bean
  public Docket api() {
    String basePackage = "br.com.magalu.challenger.scheduler.applications.controllers";
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage(basePackage))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(this.getApiInfo());
  }

  private ApiInfo getApiInfo() {
    return new ApiInfo(
        this.name,
        this.description,
        this.version,
        "Nothing terms",
        new Contact("Wesley Ribeiro", "http://tilmais.com", "wes.rsilva@gmail.com"),
        "Apache License 2.0",
        "https://www.apache.org/licenses/LICENSE-2.0",
        Collections.emptyList());
  }
}
