package tobyspring.helloboot;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
// annotation을 굳이 만들어서 쓰는 이유
bean object가 어떠한 종류인지 명시가능
web 계층인지 data 계층인지 등 어떤 역할을 하는지 표현이 가능
ex. service , controller -> stereotype annotation (스프링이 미리 만들어둔것)
 */

@Retention(RetentionPolicy.RUNTIME)  // 이 annotation이 어디까지 살아있을것인가
@Target(ElementType.TYPE)           // annotation이 적용될 대상을 지정해줄 수 있음
@Component
public @interface MyComponent {
}
