package tobyspring.study;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.assertj.core.api.Assertions.assertThat;

public class ConditionalTest {
    @Test
    void conditional(){
        ApplicationContextRunner applicationContextRunner = new ApplicationContextRunner();
        applicationContextRunner.withUserConfiguration(Config1.class)
                .run(context -> {
                    assertThat(context).hasSingleBean(MyBean.class);
                    assertThat(context).hasSingleBean(Config1.class);
                });

        new ApplicationContextRunner().withUserConfiguration(Config2.class)
                .run(context -> {
                    assertThat(context).doesNotHaveBean(MyBean.class);
                    assertThat(context).doesNotHaveBean(Config2.class);
                });
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Conditional(TrueCondition.class)
    @interface TrueConditional{}

    @Configuration
    //@Conditional(TrueCondition.class)  -> 얘를 그냥 애노테이션에 포함시켜보자
    @TrueConditional
    static class Config1{
        @Bean
        MyBean myBean(){
            return new MyBean();
        }
    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Conditional(FalseCondition.class)
    @interface FalseConditional{}


    @Configuration
    //@Conditional(FalseContion.class)
    @FalseConditional
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
    private static class FalseCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return false;
        }
    }
}
