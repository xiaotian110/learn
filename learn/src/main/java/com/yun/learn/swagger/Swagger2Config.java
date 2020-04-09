package com.yun.learn.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2文档构建配置类 通过@EnableSwagger2注解来启用Swagger2
 * 
 * @author
 */
@Configuration
@EnableWebMvc
@EnableSwagger2
public class Swagger2Config {
	// 接口文档构建配置
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				// 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现
				.select()
				// 所有的接口
				// .apis(RequestHandlerSelectors.any())
				// 指定扫描的路径
				.apis(RequestHandlerSelectors.basePackage("com.tyao.study.controller")).build().apiInfo(apiInfo());
	}

	// 接口文档信息
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("接口服务列表")
				.description("study测试")
				.termsOfServiceUrl("")
				.version("1.0")
				.build();
	}

}
