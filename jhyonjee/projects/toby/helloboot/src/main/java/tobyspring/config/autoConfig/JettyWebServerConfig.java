package tobyspring.config.autoConfig;

import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.type.AnnotatedTypeMetadata;
import tobyspring.config.MyAutoConfiguration;

/**
 * 조건을 부여해서 특정 조건이 만족되면 jetty server를 쓰게끔 설정해보자 -> 선택이 가능하게끔
 */
@MyAutoConfiguration
//@configuration -> 클래스 레벨에 붙이는 것
@Conditional(JettyWebServerConfig.JettyCondition.class)
public class JettyWebServerConfig {
    @Bean("jettyWebServerFactory")
    public ServletWebServerFactory servletWebServerFactory(){
        return new JettyServletWebServerFactory();
    }

    // 한 파일에서 보기 편하게 중첩 static class로 생성
    // Condition ineterface를 구현한 class
    static class JettyCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            // JettyWebServerConfig class를 빈으로 등록할 것인지 결정하는 것에 대한 정보를 spring container에게 알려줌
            // ConditionContext: 현재 이 컨테이너와 애플리케이션이 돌아가고 있는 환경에 대해 알 수 있음
            // AnnotatedTypeMetadata: @Conditional과 함꼐 붙은 애너테이션들의 정보를 알려주는 메타데이터

            return true; // true를 리턴하면 이 구성정보를 사용하겠다는 뜻!
        }
    }
}


