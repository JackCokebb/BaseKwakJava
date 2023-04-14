package tobyspring.helloboot;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) //애노테이션이 어디까지 살아있을 것인가
@Target(ElementType.TYPE) //지정된 타겟 위치에만 애너테이션 적용 가능
@Component
public @interface MyComponent {
}
