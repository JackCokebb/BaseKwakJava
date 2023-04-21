package tobyspring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) //default는 calss야, runtime 로딩 시엔 정보 사라짐, runtime 시에도 유지되도록 runtime으로 지정
@Target(ElementType.TYPE) // class, interface, enum에게 지정 가능
@Configuration
@ComponentScan
@EnableMyAutoConfiguration
public @interface MySpringBootApplication {

}