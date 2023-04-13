package tobyspring.myboot;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import tobyspring.config.Config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/* @Retention
자바 어노테이션의 default RetentionPolicy 는 Class임 == 어노테이션이 컴파일 된 클래스 파일 까지만 살아 있음
클래스를 메모리로 로딩할 때 그 정보가 사라짐
*/

/* @Target
TYPE : Class + Interface + Enum 이 대상이 됨
 */

/* @Import
@Component가(혹은 @Component가 붙은 어노테이션) 붙은 클래스들을 직접 추가 할 수 있음
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Configuration // 적용할 메타 애노태이션
@ComponentScan // // 적용할 메타 애노태이션
@Import(Config.class) // 스캔 대상 아니지만 직접 등록
public @interface MySpringBootAnnotation {
}
