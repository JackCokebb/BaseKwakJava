package tobyspring.study;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static org.assertj.core.api.Assertions.assertThat;

public class ConditionalTest {
    @Test
    void conditional(){


        // condition이 true인 경우 -> bean 등록이 어떻게 되는지 보자

        // Test 전용 application context
        ApplicationContextRunner applicationContextRunner = new ApplicationContextRunner();
        applicationContextRunner.withUserConfiguration(Config1.class)
                .run(context -> {   // 이렇게 만들어진 application context를 가지고 테스트 하는 것!
                    assertThat(context).hasSingleBean(MyBean.class); // 이 타입의 빈이 context안에 존재하는지 test
                    assertThat(context).hasSingleBean(Config1.class); // 이 타입의 빈이 context안에 존재하는지 test
                });


        // condition이 false인 경우
        new ApplicationContextRunner().withUserConfiguration(Config2.class)
                .run(context -> {   // 이렇게 만들어진 application context를 가지고 테스트 하는 것!
                    assertThat(context).doesNotHaveBean(MyBean.class); // 이 타입의 빈이 context안에 존재하는지 않는지 test
                    assertThat(context).doesNotHaveBean(Config2.class); // 이 타입의 빈이 context안에 존재하는지 test
                });
        // NoSuchBeanDefinitionException



    }
    @Configuration
    @Conditional(TrueCondition.class)
    static class Config1{
        @Bean
        MyBean myBean(){
            return new MyBean();
        }
    }

    @Configuration
    @Conditional(FalseContion.class)
    static class Config2{
        @Bean
        MyBean myBean(){
            return new MyBean();
        }
    }

    static class MyBean{

    }

    private static class TrueCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return true;
        }
    }
    private static class FalseContion implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return false;
        }
    }
}
