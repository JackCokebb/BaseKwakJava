package tobyspring.myboot;

import org.springframework.boot.SpringApplication;
import tobyspring.config.MySpringBootAnnotation;

@MySpringBootAnnotation
public class MybootApplication {

	public static void main(String[] args) {
		SpringApplication.run(MybootApplication.class, args);
	}
}
