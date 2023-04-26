package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

@Slf4j
public class UnCheckedAppTest {
    @Test
    void unchecked(){
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(()-> controller.request())
                .isInstanceOf(Exception.class);
    }
    @Test
    void printEx(){
        Controller controller = new Controller();
        try {
            controller.request();
        }
        catch (Exception e){
//            e.printStackTrace(); -> 좋지 않은 방식
            log.info("# ex", e); // 지금 예에서는 파라미터 (ex. class={})가 없기 때문에, 예외(e)만 파라미터에 전달하면 스택 트레이스를 로그에 출력할 수 있다.
        }
    }

    static class Controller{
        Service service = new Service();

        public void request(){
            service.logic();
        }
    }
    static class Service{
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic(){
            repository.call();
            networkClient.call();
        }
    }
    static class NetworkClient{
        public void call(){
            throw new RuntimeConnectException("# conn fail ex");
        }

    }
    static class Repository{
        public void call(){
            try {
                runSQL();
            } catch (SQLException e) {
                throw new RuntimeSQLException(e); // 기존 예외(e) 포함 -> caused by .. 로 같이 찍힘 -> 기존 예외 빠뜨리지 말자
            }

        }
        public void runSQL() throws SQLException {
            throw new SQLException("# ex");
        }
    }
    static class RuntimeConnectException extends RuntimeException{
        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class  RuntimeSQLException extends RuntimeException{
        public RuntimeSQLException() { // 기존 예외 미포함
        }

        public RuntimeSQLException(Throwable cause) { // 기존 예외 포함
            super(cause);
        }
    }
}
