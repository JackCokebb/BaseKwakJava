package hello.jdbc.exception.basic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.naming.ldap.Control;
import java.net.ConnectException;
import java.sql.SQLException;

public class CheckedAppTest {
    @Test
    void checked(){
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(()-> controller.request())
                .isInstanceOf(Exception.class);
    }

    static class Controller{
        Service service = new Service();

        public void request() throws SQLException, ConnectException { // 3. 컨ㅌ롤러 로직에서 처리할수 없는 예외이기 떄문에 던져야한다 -> 보통 controllerAdvice에서 공통으로 처리함
                                                                        // -> 보통 해결방안은 언체크예외
            service.logic();
        }
    }
    static class Service{
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() throws SQLException, ConnectException { // 2. 서비스 로직에서 처리할수 없는 예외이기 떄문에 던져야한다 -> 이렇게 throws 선언하는것이 맘에 들지 않는다는 것 -> 10개라면?!
            repository.call();
            networkClient.call();
        }
    }
    static class NetworkClient{
        public void call() throws ConnectException {
            throw new ConnectException("conn fail ex"); // 1. check exception
        }

    }
    static class Repository{
        public void call() throws SQLException {
            throw new SQLException("sql ex"); // 1. check exception
        }
    }
}
