package tobyspring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import tobyspring.config.EnableMyAutoConfiguration;
import tobyspring.config.autoConfig.DispatcherServletConfig;
import tobyspring.config.autoConfig.TomcatWebServerConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Configuration
@ComponentScan
// component annotation이 붙은, 혹은 그걸 메타 애너테이션 갖고있는 애너테이션이 붙은 클래스들을
// Import를 이용해서 뒤에다 클래스 이름을 지정해주면 구성정보에 직접 추가할 수 있음 -> 메타 애너테이션으로써 동작하게됨
//@Import({DispatcherServletConfig.class, TomcatWebServerConfig.class}) -> 8. 계속 길어지니까 최상단에서는 보기 않조음 -> @EnableMyAutoConfiguration으로 래핑
@EnableMyAutoConfiguration
public @interface MySpringBootApplication {

}
