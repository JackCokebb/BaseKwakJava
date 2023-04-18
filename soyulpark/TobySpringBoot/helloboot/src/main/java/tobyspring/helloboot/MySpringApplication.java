package tobyspring.helloboot;

import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class MySpringApplication {

    public static void run(Class<?> applicationClass, String... args) {
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext() {
            @Override
            protected void onRefresh() {
                super.onRefresh();

                ServletWebServerFactory serverFactory = this.getBean(ServletWebServerFactory.class);
                DispatcherServlet dispatcherServlet = this.getBean(DispatcherServlet.class);
                // dispatcher servlet 은 spring container 를 생성자로 전달받아야함
                dispatcherServlet.setApplicationContext(this);  // 그런데!!! 이게 없어도 됨 대체 어떻게 ?? 스프링 컨테이너가 디스패처 서블릿은 applicationContext 가 필요한 걸 알고 알아서 넣어줌

                WebServer webServer = serverFactory.getWebServer(servletContext -> {
                    servletContext.addServlet("dispatcherServlet", dispatcherServlet)
                            .addMapping("/*");
                });
                webServer.start();
            }
        };

        applicationContext.register(HellobootApplication.class); // 구성 정보를 가진 클래스를 등록해줘야 함
        applicationContext.refresh();
    }
}
