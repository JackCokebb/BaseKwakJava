package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 *  JDBC - DataSource 사용, JdbcUtils 사용
 *  이번에는 애플리케이션에 DataSource 를 적용해보자.
 *  외부에서 DataSource 를 주입 받아서 사용한다. 이제 직접 만든 DBConnectionUtil을 사용하지 않아도 된다.
 *  DataSource 는 표준 인터페이스 이기 때문에 DriverManagerDataSource 에서 HikariDataSource 로 변경되어도 해당 코드를 변경하지 않아도 된다.
 *
 * JdbcUtils 편의 메서드
 *  스프링은 JDBC를 편리하게 다룰 수 있는 JdbcUtils 라는 편의 메서드를 제공한다.
 *  JdbcUtils 을 사용하면 커넥션을 좀 더 편리하게 닫을 수 있다.
 */
@Slf4j
public class MemberRepositoryV1 {

    private final DataSource dataSource;

    public MemberRepositoryV1(DataSource dataSource) {  // DataSource interface 사용 -> 구현체가 바뀌어도 코드를 안바꿔도 된다.
        this.dataSource = dataSource;
    }

    public Member save(Member member) throws SQLException {
        String sql = "INSERT INTO member(member_id, money) VALUES (?, ?)"; // 데이터 등록

        Connection con = null;
        PreparedStatement preparedStatement = null;


        try {
            con = getConnection();
            preparedStatement = con.prepareStatement(sql);

            //sql 변수에 넣어줬던 동적 파라미터 지정 - 타입 주의!
            preparedStatement.setString(1, member.getMemberId());
            preparedStatement.setInt(2, member.getMoney());
            preparedStatement.executeUpdate();

            return member;
        } catch (SQLException e) {
            log.error("# DB error");
            throw e;
        }
        finally {
            close(con, preparedStatement, null);

        }
    }

    public Member findById(String memberId) throws SQLException {
        String sql = "SELECT * FROM MEMBER WHERE member_id = ?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try{
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, memberId);

            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Member member = new Member();
                member.setMemberId(resultSet.getString("member_id"));
                member.setMoney(resultSet.getInt("money"));
                return member;
            }
            else{
                throw new NoSuchElementException("# member not found memberId = " + memberId);
            }

        } catch (SQLException e) {
            log.error("# prepareStatement ERROR - findById()");
            throw e;
        }
        finally {
            close(connection, preparedStatement, resultSet);
        }

    }

    public void update(String memberId, int money) throws SQLException {
        String sql = "UPDATE member SET money=? WHERE member_id=?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try{
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, money);
            preparedStatement.setString(2, memberId);

            int resultSize = preparedStatement.executeUpdate(); //등록, 수정, 삭제처럼 데이터를 변경하는 쿼리는 executeUpdate()
            log.info("# result size = {}", resultSize);

        } catch (SQLException e) {
            log.error("# prepareStatement ERROR - findById()");
            throw e;
        }
        finally {
            close(connection, preparedStatement, null);
        }

    }

    public void delete(String memberId) throws SQLException {
        String sql = "DELETE FROM member WHERE member_id=?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try{
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, memberId);

            int resultSize = preparedStatement.executeUpdate(); //등록, 수정, 삭제처럼 데이터를 변경하는 쿼리는 executeUpdate()

        } catch (SQLException e) {
            log.error("# prepareStatement ERROR - findById()");
            throw e;
        }
        finally {
            close(connection, preparedStatement, null);
        }
    }

    private void close(Connection con, Statement statement, ResultSet resultSet){

        JdbcUtils.closeResultSet(resultSet);
        JdbcUtils.closeStatement(statement);
        JdbcUtils.closeConnection(con);

        // 아래 코드 대체

//        if(resultSet != null){
//            try {
//                resultSet.close();
//            } catch (SQLException e) {
//                log.error("# statement close error");
//            }
//        }
//        if(statement != null){
//            try {
//                statement.close();
//            } catch (SQLException e) {
//                log.error("# statement close error");
//            }
//        }
//
//        if(con != null){
//            try {
//                con.close();
//            } catch (SQLException e) {
//                log.error("# connection close error");
//            }
//        }

    }

    private Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        log.info("# Get connection={}, class={} v1", connection, connection.getClass());
        return connection;
    }
}
