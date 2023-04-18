package tobyspring.helloboot;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class HelloControllerTest {
    @Test
    void helloController(){
        // hello controller를 테스트하려면 hello service 오브젝트도 실행되어야하는디? -> 의존 오브젝트로부터 고립되어있지 않음
        // -> DI를 잘 활용하면 됨
        HelloController helloController = new HelloController((HelloService) name -> name); // 익명 클래스 만들기 -> (인터페이스가 하나라서) 람다로 만들기

        String ret = helloController.hello("Test");
        Assertions.assertThat(ret).isEqualTo("Test");
    }
    @Test
    void failHelloController(){ // null exception이 잘 뜨는지 확인
        HelloController helloController = new HelloController((HelloService) name->name); // test용으로 사용하는 stub 같은거를 넣어주는 것도 일종의 DI라고 할 수 있음 (수동 DI)

        Assertions.assertThatThrownBy(()->{ // 이 람다식 안에 exception이 발생할만한 코드 넣기
            helloController.hello(null);
        }).isInstanceOf(IllegalArgumentException.class);

        // 빈 문자열이 날라왔을때 테스트
        Assertions.assertThatThrownBy(()->{ // 이 람다식 안에 exception이 발생할만한 코드 넣기
            helloController.hello("");
        }).isInstanceOf(IllegalArgumentException.class);

    }
}
