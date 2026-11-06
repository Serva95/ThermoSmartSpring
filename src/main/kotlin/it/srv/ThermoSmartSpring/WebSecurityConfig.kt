package it.srv.ThermoSmartSpring

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.provisioning.JdbcUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import javax.sql.DataSource

@Configuration
@EnableWebSecurity
open class WebSecurityConfig(private val dataSource: DataSource) {

    @Bean
    open fun argon2PasswordEncoder(): Argon2PasswordEncoder =
        Argon2PasswordEncoder(16, 64, 8, 1 shl 13, 16)

    @Bean
    open fun userDetailsManager(): JdbcUserDetailsManager {
        val userDetailsManager = JdbcUserDetailsManager(dataSource)
        userDetailsManager.setUsersByUsernameQuery("select username, password, enabled from users where username = ?")
        userDetailsManager.setAuthoritiesByUsernameQuery("select username, authority from authorities where username = ?")
        return userDetailsManager
    }

    @Bean
    open fun filterChain(http: HttpSecurity, userDetailsManager: JdbcUserDetailsManager): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth -> auth
                .requestMatchers("/", "/home", "/js/**", "/stylesheets/**", "/logo.png", "/favicon.ico", "/user/registration", "/authentication", "/api/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/register").hasRole("ADMIN")
                .anyRequest().authenticated()
            }
            .formLogin { form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            }
            .sessionManagement { session -> session
                .invalidSessionUrl("/error?message=\"Session timeout, login again.\"")
                .sessionFixation { sessionFixation -> sessionFixation.none() }
                .maximumSessions(3)
                .sessionRegistry(sessionRegistry())
            }
            .logout { logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            }
            .csrf { csrf -> csrf
                .ignoringRequestMatchers("/api/**")
            }
            .cors(Customizer.withDefaults())
            .rememberMe { rm -> rm
                .alwaysRemember(true)
                .tokenValiditySeconds(3600 * 24 * 100)
            }

        return http.build()
    }

    @Bean
    open fun sessionRegistry(): SessionRegistry = SessionRegistryImpl()
}
