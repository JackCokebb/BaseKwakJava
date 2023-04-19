package tobyspring.helloboot;

import org.springframework.boot.SpringApplication;
import tobyspring.config.MySpringBootApplication;

@MySpringBootApplication
public class HelloBootApplication {
	public static void main(String[] args) {
		SpringApplication.run(HelloBootApplication.class, args);
	}
}
