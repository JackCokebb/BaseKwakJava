package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC - 같은 커넥션을 유지하여 DB 트랜잭션을 사용하기 위해 Connection을 파라미터로 넘기기
 */
@Slf4j
public class MemberRepositoryV2 {

    private final DataSource dataSource;

    public MemberRepositoryV2(DataSource dataSource) {  // DataSource interface 사용 -> 구현체가 바뀌어도 코드를 안바꿔도 된다.
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

    public Member findById(Connection connection, String memberId) throws SQLException {
        String sql = "SELECT * FROM MEMBER WHERE member_id = ?";

        //Connection connection = null; -> parameter로 넘어온 transaction 써야하기 때문에 제거
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            //connection = getConnection(); -> parameter로 넘어온 transaction 써야하기 때문에 제거
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
            //close(connection, preparedStatement, resultSet);

            // Connection은 여기서 닫지 않는다.
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(preparedStatement);
            //JdbcUtils.closeConnection(con); -> connection 닫으면 안됨, 최초로 넘겨준 곳에서 종료해야함 (service 쪽)
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

    public void update(Connection connection, String memberId, int money) throws SQLException {
        String sql = "UPDATE member SET money=? WHERE member_id=?";

        //Connection connection = null;
        PreparedStatement preparedStatement = null;

        try{
            //connection = getConnection();
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
//            close(connection, preparedStatement, null);

            JdbcUtils.closeStatement(preparedStatement);
            //JdbcUtils.closeResultSet(resultSet);
            //JdbcUtils.closeConnection(con);
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
    }

    private Connection getConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        log.info("# Get connection={}, class={} v1", connection, connection.getClass());
        return connection;
    }
}
