package tobyspring.config;


import org.springframework.context.annotation.Import;
import tobyspring.config.autoconfig.DispatcherServletConfig;
import tobyspring.config.autoconfig.TomcatServletWebServerConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({DispatcherServletConfig.class, TomcatServletWebServerConfig.class})            // 이렇게 넘겨주면 component scan 대상은 아니지만 구성 정보에 추가됨
public @interface EnableMyAutoConfiguration {
}
