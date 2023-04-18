package tobyspring.helloboot;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

// 구성 정보를 가지고 있는 클래스라는 것을 알려줘야함 > 스프링 컨테이너가 보고 이 안에 bean을 정의하는 factory method가 있겠구나 하고 bean object를 만듬
@Configuration
@ComponentScan // 이 annotation이 붙은 클래스가 포함된 패키지 포함 하위 패키지까지 @Component 가 붙은 클래스를 빈으로 등록함
public class HellobootApplication {

	public static void main(String[] args) {
		// annotation 이 붙은 자바 코드를 사용하여 구성 정보를 읽어옴
		AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext() {
			@Override
			protected void onRefresh() {
				super.onRefresh();
				ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
				WebServer webServer = serverFactory.getWebServer(servletContext -> {
					servletContext.addServlet("dispatcherServlet",
							// dispatcher sevlet 생성
							new DispatcherServlet(this)
					).addMapping("/*");
				});
				webServer.start();
			}
		};

		applicationContext.register(HellobootApplication.class); // 구성 정보를 가진 클래스를 등록해줘야 함
		applicationContext.refresh();

	}

}
