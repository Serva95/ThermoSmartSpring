package it.srv.ThermoSmartSpring

import org.springframework.context.annotation.Configuration
import org.springframework.http.CacheControl
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.util.concurrent.TimeUnit

@Configuration
open class WebMvcConfiguration : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        requireNotNull(registry)

        registry
            .addResourceHandler("/stylesheets/**")
            .addResourceLocations("classpath:/static/stylesheets/")
            .setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS))

        registry
            .addResourceHandler("*.png")
            .addResourceLocations("classpath:/static/")
            .setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS))

        registry
            .addResourceHandler("*.ico")
            .addResourceLocations("classpath:/static/")
            .setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS))

        registry
            .addResourceHandler("/js/**")
            .addResourceLocations("classpath:/static/js/")
            .setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS))
    }
}
