package cz.plichtanet.honza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/robots.txt", "/index.jsp", "/.well-known/**", "/static/**").permitAll()
                .antMatchers("/downloadPdf/**").hasRole("PDF_USER")
                .antMatchers("/dir/**", "/setPassword", "/addUser").hasRole("ADMIN")
                .antMatchers("/helloagain").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .permitAll()
                .and()
            .exceptionHandling()
                .accessDeniedPage("/403")
                .and()
            .csrf();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
/*
        // pro lokalni testovani bez databaze
        auth
            .inMemoryAuthentication()
                .withUser("jenda").password("604321192").roles("USER","PDF_USER","ADMIN");
*/
        PasswordEncoder encoder = passwordEncoder();
        auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(encoder)
/*
                .withUser("petra").password(encoder.encode("737438719")).roles("USER")
                .and()
                .withUser("kuba").password(encoder.encode("737438719")).roles("USER")
                .and()
                .withUser("jenda").password(encoder.encode("604321192")).roles("USER","ADMIN")
*/
        ;

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}