package tobyspring.helloboot;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.http.HttpHeaders;
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
			// hello 라는 이름의 서블릿(웹 요청을 받아서 처리하는 웹 프로그래밍을 담당) 등록
			servletContext.addServlet("hello", new HttpServlet() {
				@Override
				public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

					String name = req.getParameter("name");

					// 웹 응답의 3가지 요소 (상태 코드, 헤더, 바디)
					res.setStatus(HttpStatus.OK.value());
					res.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
					res.getWriter().println("Hello " + name);

				}
			}).addMapping("/hello"); // < hello 라는 url로 들어오는 요청은 이 hello 서블릿이 처리하겠다
		});
		webServer.start();
	}

}
