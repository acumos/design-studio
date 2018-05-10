package org.acumos.csvdatabroker.config;

import org.acumos.csvdatabroker.Application;
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
public class SwaggerConfiguration {
	
	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.basePackage("org.acumos.csvdatabroker.controller"))            
          .paths(PathSelectors.any())                          
          .build()
          .apiInfo(apiInfo());                                           
    }
	
	private ApiInfo apiInfo() {
		final String version = Application.class.getPackage().getImplementationVersion();
		ApiInfo apiInfo = new ApiInfo("CSV Databroker Service REST API", // title
				"Methods supporting all CSV Databroker Service.", // description
				version == null ? "version not available" : version, // version
				"Terms of service", // TOS
				new Contact("Acumos Design Studio Composition Engine Team",
						"http://acumos.org",
						"DCE.someday@acumos.org"), // Contact
				"Apache 2.0", // License
				"https://www.apache.org/licenses/LICENSE-2.0"); // License URL
		return apiInfo;
	}
}
