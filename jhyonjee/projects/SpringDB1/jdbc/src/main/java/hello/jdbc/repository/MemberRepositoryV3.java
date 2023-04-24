package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * 트랜잭션 - 트랜잭션 매니저
 * DataSourceUtils.getConnection()
 * DataSourceUtils.releaseConnection()
 * --> 트랜젝션 동기화 매니저에 접근해서 커넥션을 가져오고 닫고 하는 메소드
 */
@Slf4j
public class MemberRepositoryV3 {

    private final DataSource dataSource;

    public MemberRepositoryV3(DataSource dataSource) {  // DataSource interface 사용 -> 구현체가 바뀌어도 코드를 안바꿔도 된다.
        this.dataSource = dataSource;
    }

    public Member save(Member member) throws SQLException {
        String sql = "INSERT INTO member(member_id, money) VALUES (?, ?)";

        Connection con = null;
        PreparedStatement preparedStatement = null;


        try {
            con = getConnection();
            preparedStatement = con.prepareStatement(sql);

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

    // Connection을 파라미터로 넘겨줬던 것은 이제 필요없다 -> 트랜잭션 동기화 매니저를 사용하기 때문
//    public Member findById(Connection connection, String memberId) throws SQLException {
//        String sql = "SELECT * FROM MEMBER WHERE member_id = ?";
//
//        PreparedStatement preparedStatement = null;
//        ResultSet resultSet = null;
//
//        try{
//            preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setString(1, memberId);
//
//            resultSet = preparedStatement.executeQuery();
//            if(resultSet.next()){
//                Member member = new Member();
//                member.setMemberId(resultSet.getString("member_id"));
//                member.setMoney(resultSet.getInt("money"));
//                return member;
//            }
//            else{
//                throw new NoSuchElementException("# member not found memberId = " + memberId);
//            }
//
//        } catch (SQLException e) {
//            log.error("# prepareStatement ERROR - findById()");
//            throw e;
//        }
//        finally {
//            JdbcUtils.closeResultSet(resultSet);
//            JdbcUtils.closeStatement(preparedStatement);
//        }
//
//    }



    public void update(String memberId, int money) throws SQLException {
        String sql = "UPDATE member SET money=? WHERE member_id=?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try{
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, money);
            preparedStatement.setString(2, memberId);

            int resultSize = preparedStatement.executeUpdate();
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

            int resultSize = preparedStatement.executeUpdate();

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
        //JdbcUtils.closeConnection(con);

        // 주의! 트랜잭션 동기화를 사용하려면 DataSourceUtils를 사용해야 한다.
        DataSourceUtils.releaseConnection(con, dataSource); // -> release할때 트랜잭션 동기화 매니저에서 가져다 쓴 커넥션이먄 딛지 않고 넘긴다
    }

    private Connection getConnection() throws SQLException {
        //Connection connection = dataSource.getConnection();

        //주의! 트랜잭션 동기화를 사용하려면 DataSourceUtils를 사용해야 한다.
        Connection connection = DataSourceUtils.getConnection(dataSource);
        log.info("# Get connection={}, class={} v1", connection, connection.getClass());
        return connection;
    }
}
