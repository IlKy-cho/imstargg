package com.imstargg.core.api.support;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

/**
 * 참고: https://taetaetae.github.io/2020/03/08/spring-rest-docs-in-spring-boot/#springprofile-에-따라-spring-rest-docs-url-을-접근-가능불가능-할-수-있게-한다
 */
@Profile("live")
@WebFilter(urlPatterns = "/docs/**")
public class SpringRestDocsAccessFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(SpringRestDocsAccessFilter.class);

    @PostConstruct
    public void init() {
        log.debug("initialized");
    }

    @Override
    public void doFilter(
            ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain
    ) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        chain.doFilter(servletRequest, response);
    }
}
