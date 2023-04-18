package tobyspring.helloboot;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // runtime으로 지정안하면 default가 CLASS임 -> class compile까지는 애너테이션이 살아있지만 메모리에 올라가는 런타임에는 죽음
@Target(ElementType.TYPE) // TYPE: class, interface, enum에 적용됨
@Configuration // meta annotation
@ComponentScan // meta annotation
public @interface MySpringBootAnnotation {

}
