package cz.plichtanet.honza;

import org.springframework.context.annotation.*;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@PropertySource("classpath:app.properties")
@ComponentScan(basePackages = "cz.plichtanet.honza")
@ImportResource("classpath*:aws.xml")
public class HelloWorldConfiguration {

}