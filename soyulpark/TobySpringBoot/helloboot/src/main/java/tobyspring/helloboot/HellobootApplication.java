package tobyspring.helloboot;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HellobootApplication {

	public static void main(String[] args) {
		ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
		WebServer webServer = serverFactory.getWebServer(servletContext -> {
			servletContext.addServlet("frontcontroller", new HttpServlet() {
				@Override
				public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

					// 인증, 보안, 다국어, 공통 기능 등을 담당하는 프론트 컨트롤러
					if (req.getRequestURI().equals("/hello") && req.getMethod().equals(HttpMethod.GET.name())){
						String name = req.getParameter("name");

						res.setStatus(HttpStatus.OK.value());
						res.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
						res.getWriter().println("Hello " + name);
					} else if (req.getRequestURI().equals("/user")) {
						//
					} else {
						res.setStatus(HttpStatus.NOT_FOUND.value());
					}
				}
			}).addMapping("/*"); // < 모든 요청을 이 서블릿이 처리하겠다
		});
		webServer.start();
	}

}
