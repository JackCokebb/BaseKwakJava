package tobyspring.config;

import org.springframework.context.annotation.Configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
// 12. proxyBeanMethods = false로 했다는 것은 이 MyAutoConfiguration 밑에 있는 config들은 proxy로 만들어지지 않고 사용된다는 뜻
//     @Configuration 안에 빈이 생성될때 주입되어야 할것이 업으면 주입할 오브젝트가 하나의 객체로 재사용되는지 확인할 필요 없게되고, 그럼 그걸 확인해주던 프록시도 필요 없게되는 것
@Configuration(proxyBeanMethods = false)
public @interface MyAutoConfiguration {

}

