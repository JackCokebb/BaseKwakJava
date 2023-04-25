package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class CheckedTest {

    // 5. test 실행 - 예외 캐치
    @Test
    void checked_catch(){
        Service service = new Service();
        service.callCatch();
    }

    // 7. Test 2 실행 - 예외 안잡고 던짐
    @Test
    void checked_throw(){
        Service service = new Service();
        Assertions.assertThatThrownBy(() -> service.callThrow()).isInstanceOf(MyCheckedException.class);
    }


    /**
     * 1. Exception을 상속받은 예외는 체크예외가 된다.
     */
    static class MyCheckedException extends Exception{
        public MyCheckedException(String message) {
            super(message);
        }
    }

    /**
     * 체크 예외는
     * 예외를 잡아서 처리하거나, 던지거나 둘 중 하나를 필수로 선택해야한다.
     */
    static class Service{
        Repository repository = new Repository();
        /**
         * 3. 예외를 잡아서 처리하는 코드
         */
        public void callCatch(){
            try {
                repository.call(); // -> 여기서 repository가 던진 예외 발생 -> 컴파일러가 체크해서 예외난다고 알려줌 (그래서 체크 예외임) -> service도 이걸 잡거나 던져야함
            } catch (MyCheckedException e) {
                // 4. 예외 처리 로직
                log.info("# 예외 처리, message={}", e.getMessage(),e); // exception을 stacktrace로 출력할때는 마지막에 exception을 넣어주면 됨
                // 이후 코드는 정상 흐름으로 돌아감
            }
        }

        /**
         * 체크 예외를 밖으로 던지는 코드
         * 체크 예외는 예외를 잡지 않고 밖으로 던지려면 throws 예외를 메서드에 필수로 선언해야한다.
         * throws Exception 으로 모든 Exception 하위 예외들을 던질 수 있지만 좋은 코드는 아님!!!
         * @throws MyCheckedException
         */
        public void callThrow() throws MyCheckedException {
            repository.call();
        }
    }
    static class Repository{
        public void call() throws MyCheckedException {
            throw new MyCheckedException("ex");
            // 2. 여기서 이 예외를 잡거나 던지지 않으면 컴파일러가 잡아서 컴파일이 되지 않음
            // 여기서는 잡지 않고 밖으로 던짐 -> MyCheckedException
        }
    }
}
