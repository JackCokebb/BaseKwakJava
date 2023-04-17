package tobyspring.config;

import org.springframework.context.annotation.Configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
// 1. @Configuration을 메타 애너테이션으로 가지고 있으니 기존에 @Configuration을 붙이고 있던 애들한테 붙여줄 수 있겠지?
// 4. 대신 @Configuration의 element 값을 변경해 줘야함
//      proxyBeanMethods = false -> @MyAutoConfiguration이 붙어서 동적으로 로딩되는 config들은 기존에 썼던것처럼 그냥 @Configuration이 적용된것이 아니라
//                                  proxyBeanMethods를 false로 바꾼 Configuration이 적용된다는  -> 근데 이게 뭔데??
@Configuration(proxyBeanMethods = false)
public @interface MyAutoConfiguration {

}
// 3. ...MyAutoConfiguration.imports 파일에 있는 config 파일들에는 @MyAutoConfiguration를 붙여주는것이 원칙 -> config class로 이동


// 5. @Configuration이 붙은 클래스의 독특한 특징에 대해 이해할 필요가 있다
//      우리가 직접 config class를 애플리케이션 코드에 넣고 원하는 빈을 등록할 일이 많다