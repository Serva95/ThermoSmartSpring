package it.srv.ThermoSmartSpring

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class ThermoSmartSpringApplication {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(ThermoSmartSpringApplication::class.java, *args)
        }
    }
}
