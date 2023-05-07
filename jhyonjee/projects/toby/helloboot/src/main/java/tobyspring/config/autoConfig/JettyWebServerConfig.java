package tobyspring.config.autoConfig;

import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import tobyspring.config.MyAutoConfiguration;

/**
 * 조건을 부여해서 특정 조건이 만족되면 jetty server를 쓰게끔 설정해보자 -> 선택이 가능하게끔
 */
@MyAutoConfiguration // -> 이 애너테이션은 autoConfiguration이 로딩되는 메커니즘을 이용해서 config를 등록되게 만들 것이니 import라는 파일안에 추가해줘야함. -> 자동구성 대상이되는 config를 지정해주는 것
public class JettyWebServerConfig {
    @Bean("jettyWebServerFactory") // 이름을 지정하지 않으면 메소드명으로 bean이 생성됨 -> 이번에는 지정해보자.
    public ServletWebServerFactory servletWebServerFactory(){
        return new JettyServletWebServerFactory();
    }
}

// 이대로 실행하면 오류남 -> web server가 두개이기 때문
// 조건을 걸어주자!
