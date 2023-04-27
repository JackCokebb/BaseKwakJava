package hello.jdbc.exception.translator;

import hello.jdbc.connection.ConnectionConst;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class SpringExceptionTranslatorTest {

    DataSource dataSource;

    @BeforeEach
    void init(){
        dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
    }

    @Test
    void sqlExceptioonErrorCode(){
        String sql = "select bad grammar"; // 잘못된 sql 문.

        try{
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeQuery();
        }
        catch (SQLException e){
            assertThat(e.getErrorCode()).isEqualTo(42122); // 이런걸 언제 다 확인해서 다른 예외로 포장하나?!
            int errorCode = e.getErrorCode();
            log.info("# errorCode={}", errorCode);
            log.info("# error", e);
        }
    }

    @Test
    void exceptionTranslator(){
        String sql = "select bad grammar";

        try{
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeQuery();
        }
        catch (SQLException e){
            assertThat(e.getErrorCode()).isEqualTo(42122);

            // 예외 변환 -> 주로 repository에서 변환
            SQLErrorCodeSQLExceptionTranslator sqlErrorCodeSQLExceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);// Spring이 제공하는 예외 변환기.
            DataAccessException resultEx = sqlErrorCodeSQLExceptionTranslator.translate("select", sql, e); // .translate("작업명", sql, exception) -> 이 예외를 분석해서 DataAccessException을 반환해줌( 이렇게 하면 적절한 스프링 데이터 접근 계층의 예외로 변환해서 반환해줌.)
            log.info("# resultEx", resultEx);
            assertThat(resultEx.getClass()).isEqualTo(BadSqlGrammarException.class); // 잘 변환 되았는지 확인 -> 변환된 예외를 서비스에서 가져다 쓰면됨
        }
    }
    /*
    스프링 예외 추상화 덕분에 특정 기술에 종속적이지 않게 되었다.
    이제 JDBC에서 JPA같은 기술로 변경되어도 예외로 인한 변경을 최소화 할 수 있다.
    향후 JDBC에서 JPA로 구현 기술을 변경하더라도, 스프링은 JPA 예외를 적절한 스프링 데이터 접근 예외로 변환해준다.
     */
}
