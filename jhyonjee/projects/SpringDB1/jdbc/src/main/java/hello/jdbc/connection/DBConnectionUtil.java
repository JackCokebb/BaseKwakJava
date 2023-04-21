package hello.jdbc.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j // log 생성
public class DBConnectionUtil {
    public static Connection getConnection(){  // java JDBC 표준 인터페이스가 제공하는 connection
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD); // h2 가 구현한 Connection을 가져옴 org.h2.jdbc.JdbcConnection
            log.info("# Get Connection={}, class={}", connection, connection.getClass());
            return connection;

        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
