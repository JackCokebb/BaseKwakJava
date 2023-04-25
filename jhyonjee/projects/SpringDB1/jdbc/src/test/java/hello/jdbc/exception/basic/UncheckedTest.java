package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class UncheckedTest {
    // 4. test 진행
    @Test
    void unchecked_catch(){
        Service service = new Service();
        service.callCatch();
    }

    // 6. test (throw) 진행
    @Test
    void unchecked_throw(){
        Service service = new Service();
        Assertions.assertThatThrownBy(() -> service.callThrow()).isInstanceOf(MyUncheckedException.class);

    }

    // 1. exception 정의
    /**
     * RuntimeException을 상속받은 예외는 언체크예외가 된다.
     */
    static class MyUncheckedException extends RuntimeException{
        public MyUncheckedException(String message) {
            super(message);
        }
    }

    /**
     * 3. 서비스 생성
     * Unchecked 예외는
     * 예외를 잡거나, 던지지 않아도 된다.
     * 예외를 잡지 않으면 자동으로 밖으로 던진다.
     */
    static class Service{
        Repository repository = new Repository(); // 단순 test니까 주입하지 말자

        /**
         * 필요한 경우 예외를잡앗 처리하면 된다.
         */
        public void callCatch(){
            try{
                repository.call();
            }
            catch(MyUncheckedException e){
                // 예외 처리 로직
                log.info("# 예외 처리, message={}", e.getMessage(), e);
            }

            // 예외가 날라왔을텐데 컴파일러 경고 안뜸 -> 언체크다~
            // 여기서 가만히 두면 자동으로 throws가 붙어서 밖으로 던진다
            // 이번에는 catch 해보자

        }

        /**
         * 5. 예외를 잡지 않아도 된다. 자엽스럽게 상위로 넘어간다.
         * 체크 예외와 다르게 throws 예외 선언을 하지 않아도 된다.
         */
        public void callThrow(){
            repository.call();
        }
    }
    // 2. repository 생성
    static class Repository{
        public void call(){ // checked exception과 다르게 throws를 안달아도 되네
            throw new MyUncheckedException("ex");
        }
    }
}
