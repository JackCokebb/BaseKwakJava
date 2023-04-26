package hello.jdbc.exception.basic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

public class UnCheckedAppTest {
    @Test
    void unchecked(){
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(()-> controller.request())
                .isInstanceOf(Exception.class);
    }

    static class Controller{
        Service service = new Service();

        public void request() throws SQLException, ConnectException {
            service.logic();
        }
    }
    static class Service{
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic(){ // throws SQLException, ConnectException { -> 이제 이 예외들 다 뗼수있지
            repository.call();
            networkClient.call();
        }
    }
    static class NetworkClient{
        public void call(){ // throws ConnectException { -> 이제 이 예외들 다 뗼수있지
            throw new RuntimeConnectException("# conn fail ex");
        }

    }
    static class Repository{
        public void call(){
            try {
                runSQL(); // 3. repository 내부적으로 일단 runSQL 실행 -> 근데 여기서 이 체크 예외 잡을 것임 -> 잡아서 밖으로 던질때는 런타임 에러(언체크)로 바꿔서 던짐
            } catch (SQLException e) {
                throw new RuntimeSQLException(e); // 원래 error도 넣어줘야 원래 예외의 stacktrace도 포함해서 추적가능
            }

        }
        public void runSQL() throws SQLException { // 2. SQL을 실행한다고 가정
            throw new SQLException("# ex"); // check 예외 터짐
        }
    }
    static class RuntimeConnectException extends RuntimeException{ // 1. uncheck 에외 정의
        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class  RuntimeSQLException extends RuntimeException{  // 1. uncheck 에외 정의

        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }
}
