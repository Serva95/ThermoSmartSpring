package it.srv.ThermoSmartSpring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from users where username = ?")
                .authoritiesByUsernameQuery("select username, authority from authorities where username = ?")
                .passwordEncoder(argon2PasswordEncoder());
    }

    @Bean
    public Argon2PasswordEncoder argon2PasswordEncoder() { return new Argon2PasswordEncoder(16,64,8,1<<13,16);}

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http    .authorizeHttpRequests(authorize -> {
                    try {
                        authorize
                                .requestMatchers("/", "/home", "/register", "/js/**", "/stylesheets/**", "/logo.png", "/favicon.ico", "/user/registration", "/authentication", "/api/**")
                                .permitAll()
                                .anyRequest().authenticated()
                                .and()
                                .formLogin()
                                .loginProcessingUrl("/login")
                                .loginPage("/login")
                                .defaultSuccessUrl("/",true)
                                .permitAll()
                                .and()
                                .sessionManagement()
                                .invalidSessionUrl("/error?message=\"Session timeout, login again.\"")
                                .maximumSessions(3)
                                .sessionRegistry(sessionRegistry())
                                .and()
                                .sessionFixation()
                                .none()
                                .and()
                                .logout()
                                .logoutSuccessUrl("/login?logout")
                                .permitAll();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        http    .headers()
                .and()
                .cors()
                .and()
                .rememberMe()
                .alwaysRemember(true)
                .tokenValiditySeconds(3600*24*100)
                .and()
                .httpBasic()
                .disable()
        ;

        http    .csrf((csrf) -> csrf
                .ignoringRequestMatchers("/api/**")
        );

        return http.build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}