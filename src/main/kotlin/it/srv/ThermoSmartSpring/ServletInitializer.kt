package it.srv.ThermoSmartSpring

import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

open class ServletInitializer : SpringBootServletInitializer() {

    override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder =
        application.sources(ThermoSmartSpringApplication::class.java)
}
