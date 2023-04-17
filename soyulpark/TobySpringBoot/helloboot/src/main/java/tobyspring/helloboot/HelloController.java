package tobyspring.helloboot;

import java.util.Objects;

public class HelloController {
    SimpleHelloService helloService = new SimpleHelloService();
    public String hello(String name) {
        // null 인지 체크해서 null 이면 예외를 던져줌
        return helloService.sayHello(Objects.requireNonNull(name));
    }
}
