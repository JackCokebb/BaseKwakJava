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
//@Import({DispatcherServletConfig.class, TomcatWebServerConfig.class})
@Import(MyAutoConfigImportSelector.class) // MyAutoConfigImportSelector 이 안의 메소드를 실행시켜서 리턴받은 결과물들, string으로된 config class들만 로딩하도록 함
public @interface EnableMyAutoConfiguration {
}
