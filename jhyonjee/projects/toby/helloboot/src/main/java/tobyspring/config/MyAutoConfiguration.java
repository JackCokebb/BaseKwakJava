package tobyspring.config;

import org.springframework.context.annotation.Configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Configuration  // 1. 이거 사용
public @interface MyAutoConfiguration {
    /*
    2. 이 애너테이션 이름으로된 설정 파일을 하나 만들자 -> 이 안에 자동 구성 방식에 사용할 config class의 목록을 넣을거임
    3. MyAutoConfigImportSelector 코드 수정
     */
}
