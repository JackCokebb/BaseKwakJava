package tobyspring.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;

/*
 ComponentScan이 붙은 클래스의 패키지가 컴포넌트 스캔 시작 위치가 됨
 ComponentScan이 MybootApplication에 붙어있으니까 myboot 패키지가 컴포넌트 스캔 시작 위치가 됨
 config 패키지에 있는 Config 클래스는 컴포넌트 스캔 대상에서 제외됨
 */


@Configuration
public class Config {

}
