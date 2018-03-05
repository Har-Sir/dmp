package com.uuzu.chinadep.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by lixing on 2017/4/26.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * swagger摘要bean
     * @return
     */
    @Bean
    public Docket restApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //.apis(RequestHandlerSelectors.basePackage("com.uuzu.chinadep.web"))
                .paths(PathSelectors.any())
                .build()
                ;
        return docket;
    }
    /**
     * API文档主信息对象
     * @return
     */
    private ApiInfo apiInfo(){
        ApiInfo apiInfo= (new ApiInfoBuilder())
                .title("dmp-chinadep")
                .description("提供基于用户画像的数据服务")
                .termsOfServiceUrl("http://localhost:10000/")
                .contact(new Contact("金宝","","zhoujin@youzu.com"))
                .version("1.0")
                .build();
        return apiInfo;
    }

}
