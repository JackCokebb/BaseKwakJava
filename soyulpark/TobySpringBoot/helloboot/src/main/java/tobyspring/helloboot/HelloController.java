package tobyspring.helloboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

@RequestMapping("/hello")
@MyComponent
public class HelloController {

    // 직접 object 생성 > 생성자 파라미터로 주입 받는 코드
    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @GetMapping()
    @ResponseBody
    public String hello(String name) {
        // null 인지 체크해서 null 이면 예외를 던져줌
        return helloService.sayHello(Objects.requireNonNull(name));
    }
}
