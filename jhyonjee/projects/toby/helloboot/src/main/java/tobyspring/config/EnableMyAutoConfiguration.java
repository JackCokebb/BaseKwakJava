package tobyspring.config;

import org.springframework.context.annotation.Import;
import tobyspring.config.autoConfig.DispatcherServletConfig;
import tobyspring.config.autoConfig.TomcatWebServerConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({DispatcherServletConfig.class, TomcatWebServerConfig.class}) //@Import도 애너테이션의 메타 애너테이션으로 사용 가능
public @interface EnableMyAutoConfiguration {
}
