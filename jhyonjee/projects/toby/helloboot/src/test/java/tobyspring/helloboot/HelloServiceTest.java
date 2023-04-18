package tobyspring.helloboot;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


// 4. 한번 더 확장 가능? -> 가능, 다만 메타 애너테이션의 타겟에 ANNOTATION_TYPE이 포함되어야함 -> 메타 애너테이션으로 쓰겠다는 뜻
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@UnitTest // 메타 애너테이션으로써 @UnitTest 부여
@interface FastUnitTest{

}

// 2. 애너테이션 만들어보자
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Test // 메타 애너테이션으로써 @Test 부여
@interface UnitTest{

}

public class HelloServiceTest {
    //@Test // -> 1. JUnit test 하는 애너테이션 -> 조금 더 의미를 확장할 수 없을까? 이름도 좀 바꾸고? -> 만들면 그만이야
    @UnitTest // 3. 만든 애너테이션 사용
    void simpleHelloService(){
        SimpleHelloService helloService = new SimpleHelloService();
        String ret = helloService.sayHello("Test");

        Assertions.assertThat(ret).isEqualTo("Hello Test");
    }

    @Test
    void helloDecorator(){
        HelloDecorator helloDecorator = new HelloDecorator(name -> name);
        String ret = helloDecorator.sayHello("Test");
        Assertions.assertThat(ret).isEqualTo("*Tets*");
    }
}
