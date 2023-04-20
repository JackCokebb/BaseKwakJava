package hello.jdbc.connection;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {
    @Test
    void driverManager() throws SQLException {
        // driver manager로 connect 했기 때문에 매번 신규 connection 생성
        Connection connection1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection connection2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        log.info("# Connection={}, Class={}", connection1, connection1.getClass());
        log.info("# Connection={}, Class={}", connection2, connection2.getClass()); // 두 개가 서로 다른 connection인 것 확인
    }

    // spring이 제공하는 DataSource 인터페이스가 적용이된 driver manager 사용
    // DataSource 인터페이스는 여러 커넥션 풀과 드라이버를 추상화 해둔 인터페이스
    @Test
    void dataSourceDriverManager() throws SQLException {
        // driver manager로 connect 했기 때문에 매번 신규 connection 생성
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        useDataSource(driverManagerDataSource);
    }

    private void useDataSource(DataSource dataSource) throws SQLException {
        Connection connection1 = dataSource.getConnection();
        Connection connection2 = dataSource.getConnection();

        log.info("# Connection={}, Class={}", connection1, connection1.getClass());
        log.info("# Connection={}, Class={}", connection2, connection2.getClass());
    }

}
