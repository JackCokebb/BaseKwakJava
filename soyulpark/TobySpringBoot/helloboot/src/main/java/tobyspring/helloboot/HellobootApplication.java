package tobyspring.helloboot;

import org.springframework.boot.SpringApplication;
import tobyspring.config.MySpringBootApplicationAnnotation;

@MySpringBootApplicationAnnotation
public class HellobootApplication {

	public static void main(String[] args) {

		SpringApplication.run(HellobootApplication.class, args);
	}

}
