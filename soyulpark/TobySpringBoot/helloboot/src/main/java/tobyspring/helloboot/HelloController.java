package tobyspring.helloboot;

import java.util.Objects;

public class HelloController {

    // 직접 object 생성 > 생성자 파라미터로 주입 받는 코드
    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }
    public String hello(String name) {
        // null 인지 체크해서 null 이면 예외를 던져줌
        return helloService.sayHello(Objects.requireNonNull(name));
    }
}
