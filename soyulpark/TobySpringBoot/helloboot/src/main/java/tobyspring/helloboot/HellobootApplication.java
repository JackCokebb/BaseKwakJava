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
		GenericWebApplicationContext applicationContext = new GenericWebApplicationContext() {
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

		applicationContext.registerBean(HelloController.class);
		applicationContext.registerBean(SimpleHelloService.class);
		applicationContext.refresh();

	}

}
