package tobyspring.helloboot;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class HelloControllerTest {

    // 성공 case
    @Test
    void helloController() {
        HelloController helloController = new HelloController(name -> name);

        String ret = helloController.hello("Test");

        Assertions.assertThat(ret).isEqualTo("Test");
    }

    // 실패 case
    @Test
    void failsHelloController() {
        HelloController helloController = new HelloController(name -> name);

        // null을 전달했을 경우 NullPointerException 가 발생한다
        Assertions.assertThatThrownBy(() -> {
            helloController.hello(null);
        }).isInstanceOf(NullPointerException.class);
    }
}