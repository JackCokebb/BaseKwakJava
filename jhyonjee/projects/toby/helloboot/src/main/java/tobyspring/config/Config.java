package tobyspring.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class Config {
	/*
	Tomcat 만드는 팩토리 빈과 디스패쳐 서블릿 빈은 유저 구성정보에  포함시키지 않아야함
	즉 spring boot style로 application을 만들때는 애플리케이션 패키지 밑에  직접 빈으로 등록하는 일을
	하지 않아도 뭔가 자동으로 되어야함
	1. component scan 대상에서 제외 -> component scan 시작하는 패키지에서 제거 (tobyspring.config로 이동)
	2. 옮겼으니 서블렛 컨테이너가 빈으로 등록안되서 앱 실행하면 못찾을거야 -> component scan 대상이 아니더라도 구성정보에 포함이 되야함
	3. application 구성정보의 시작점은 HellobootApplication의 @MySpringApplication 부터임 -> 옮긴 config는 어떻게 등록하지??? -> @Import 사용 (@MysSpringApplication에다기)
	4. component scan이랑 뭐가 다른거지 ?? 차차 설명 예정
	5. 이번엔 이 두 개의 빈 config를 분리할거임 -> 둘이 같이 가야되는거 아니냐? -> 아니다. 톰캣 대신 다른 서블릿 컨테이너를 쓰고 싶은 경우도 있을 수 있음 -> Config 파일 분리
	6. 분리했으니 import했던 것도 분리해주자
	7. import로 등록된 config 쿨래스는 자동 구성 대상으로 삼을거니까 패키지도 분리해보자 -> autoConfig package
	8. autoConfig package에 config 파일들이 많아지면 @MySpringBootApplication에 import를 일일히 해주기에 너무 번거롭고 길어질 것 -> 최상단에선 보기 안좋으니 새 애너테이션을 부여해서 래핑
	9. 순수하게 애플리케이션 로직을 가지고있는 클래스외에 프레임워크나 라이브러리처럼 재사용이 가능한 부분들은 애플리케이션 패키지에서 제거해보자
	    @MySpringBootApplication -> config package로 이동 -> helloboot package에는 순수 애플리케이션 로직을 담은 유저 구성 정보로 사용이될 클래스만 남음

	 */
//  @Bean
//	public ServletWebServerFactory servletWebServerFactory(){
//		return new TomcatServletWebServerFactory();
//	}
//	@Bean
//	public DispatcherServlet dispatcherServlet(){
//		return new DispatcherServlet();
//	}
}
