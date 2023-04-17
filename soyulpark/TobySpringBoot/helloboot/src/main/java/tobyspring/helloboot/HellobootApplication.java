package tobyspring.helloboot;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HellobootApplication {

	public static void main(String[] args) {
		// 스프링 컨테이너 생성 Applicationcontext : application을 구성하는 정보(구성 빈, 리소스 접근 방법, 내부 이벤트 전달/구독 등) 이게 스프링 컨테이너가 된다
		GenericWebApplicationContext applicationContext = new GenericWebApplicationContext();
		// object를 직접 넣어주기보단 어떤 class를 이용해서 bean object 를 생성할지 metadata 를 넣어줌
		applicationContext.registerBean(HelloController.class); // bean 등록 (bean 클래스 지정)
		applicationContext.registerBean(SimpleHelloService.class); // 정확히 어떤 class 로 bean 을 등록할지 적어줘야 함
		applicationContext.refresh(); // applicationContext 가 bean object 를 다 만들어줌


		ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
		WebServer webServer = serverFactory.getWebServer(servletContext -> {
			servletContext.addServlet("dispatcherServlet",
								// dispatcher sevlet 생성
								new DispatcherServlet(applicationContext)
					).addMapping("/*"); // < 모든 요청을 이 서블릿이 처리하겠다
		});
		webServer.start();
	}

}
