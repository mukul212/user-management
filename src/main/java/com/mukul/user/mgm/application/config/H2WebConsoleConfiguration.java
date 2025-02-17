package com.mukul.user.mgm.application.config;

import org.h2.server.web.WebServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class H2WebConsoleConfiguration {
    @Bean
    ServletRegistrationBean h2servletRegistration() {

        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());

        registrationBean.addUrlMappings("/console/*");

        return registrationBean;

    }
}
