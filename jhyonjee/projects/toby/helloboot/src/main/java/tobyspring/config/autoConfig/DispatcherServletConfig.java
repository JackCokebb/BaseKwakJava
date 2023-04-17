package tobyspring.config.autoConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import tobyspring.config.MyAutoConfiguration;

//@Configuration
@MyAutoConfiguration // 2. 이 안에도 @Configuration이 있으니 일반적인 config file로 동작할거임
public class DispatcherServletConfig {
    @Bean
    public DispatcherServlet dispatcherServlet(){
        return new DispatcherServlet();
    }
}
