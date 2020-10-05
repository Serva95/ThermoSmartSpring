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

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from users where username = ?")
                .authoritiesByUsernameQuery("select username, authority from authorities where username = ?")
                .passwordEncoder(argon2PasswordEncoder());
    }

    @Autowired
    public Argon2PasswordEncoder argon2PasswordEncoder() { return new Argon2PasswordEncoder(16,64,8,1<<13,16);}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/home", "/register", "/js/**", "/stylesheets/**", "/logo.png", "/user/registration", "/authentication").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .loginPage("/login")
                .defaultSuccessUrl("/",true)
                .permitAll()
                .and()
                .rememberMe().key("remember")
                .and()
                .sessionManagement()
                .invalidSessionUrl("/invalidSession.html")
                .maximumSessions(1)
                .sessionRegistry(sessionRegistry())
                .and()
                .sessionFixation()
                .none()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .headers()
                .and()
                .rememberMe()
                .alwaysRemember(true)
                .tokenValiditySeconds(3600*24*100)
                .and()
                .httpBasic()
                .disable()
        ;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}