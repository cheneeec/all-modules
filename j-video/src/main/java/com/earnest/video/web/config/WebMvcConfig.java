package com.earnest.video.web.config;


import org.springframework.context.annotation.Configuration;

import org.springframework.web.reactive.config.DelegatingWebFluxConfiguration;
import org.springframework.web.reactive.config.EnableWebFlux;


@EnableWebFlux
@Configuration
public class WebMvcConfig  extends DelegatingWebFluxConfiguration {

    /*
    @Autowired
    private Optional<PageableHandlerMethodArgumentResolverCustomizer> pageableResolverCustomizer;

    @Autowired
    private Optional<SortHandlerMethodArgumentResolverCustomizer> sortResolverCustomizer;

    @Override
    public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
        configurer.addCustomResolver((HandlerMethodArgumentResolver) new PageableHandlerMethodArgumentResolver());

    }



    @Bean
    public PageableHandlerMethodArgumentResolver pageableResolver() {

        PageableHandlerMethodArgumentResolver pageableResolver = //
                new PageableHandlerMethodArgumentResolver(sortResolver());
        customizePageableResolver(pageableResolver);
        return pageableResolver;
    }

    @Bean
    public SortHandlerMethodArgumentResolver sortResolver() {

        SortHandlerMethodArgumentResolver sortResolver = new SortHandlerMethodArgumentResolver();
        customizeSortResolver(sortResolver);
        return sortResolver;
    }

    protected void customizePageableResolver(PageableHandlerMethodArgumentResolver pageableResolver) {
        pageableResolverCustomizer.ifPresent(c -> c.customize(pageableResolver));
    }

    protected void customizeSortResolver(SortHandlerMethodArgumentResolver sortResolver) {
        sortResolverCustomizer.ifPresent(c -> c.customize(sortResolver));
    }*/

}
