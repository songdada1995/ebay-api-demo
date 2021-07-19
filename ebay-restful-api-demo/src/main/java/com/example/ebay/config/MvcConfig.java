package com.example.ebay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/static/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/static/img/**").addResourceLocations("classpath:/static/img/");
        registry.addResourceHandler("/static/public/**").addResourceLocations("classpath:/static/public/");
        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
    }

    /**
     * 视图解析
     *
     * @return
     */
    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/");
        viewResolver.setSuffix(".html");
        return viewResolver;
    }

    /**
     * 映射跳转
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }

}
