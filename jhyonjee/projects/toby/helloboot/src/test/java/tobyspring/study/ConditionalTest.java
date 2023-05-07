package tobyspring.study;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

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
    @Conditional(BooleanCondition.class)
    @interface BooleanConditional{
        boolean value(); // single element만 사용하고자 할 때 사용 가능
    }  // annotation의 element값을 지정할 수 있도록 만들어 보자

    @Configuration
    @BooleanConditional(true)
    static class Config1{
        @Bean
        MyBean myBean(){
            return new MyBean();
        }
    }


    @Configuration
    @BooleanConditional(false)
    static class Config2{
        @Bean
        MyBean myBean(){
            return new MyBean();
        }
    }

    static class MyBean{

    }

    private static class BooleanCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            // @BooleanConditional(false) 처럼 조건 (ex. false)에 따라 처리되도록 만들자
            Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(BooleanConditional.class.getName());// Annotation에 붙어있는 elements == attributes를 다 가져오는 것
            Boolean value = (Boolean)annotationAttributes.get("value");
            return value;
        }
    }

}
