package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.ex.MyDbException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * 예외 누수 문제 해결
 * 체크 예외를 런타임 예외로 변경
 * MemberRepository 인터페이스 사용
 * throws SQLException 제거
 */
@Slf4j
public class MemberRepositoryV4_1 implements MemberRepository{

    private final DataSource dataSource;

    public MemberRepositoryV4_1(DataSource dataSource) {  // DataSource interface 사용 -> 구현체가 바뀌어도 코드를 안바꿔도 된다.
        this.dataSource = dataSource;
    }

    @Override
    public Member save(Member member){// throws SQLException {  -> 2. 이제 던질 필요 없으니 제거
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
            throw new MyDbException(e); // 1. 감싸자.
        }
        finally {
            close(con, preparedStatement, null);

        }
    }

    @Override
    public Member findById(String memberId){// throws SQLException {
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
            throw new MyDbException(e); // 1. 감싸자.
        }
        finally {
            close(connection, preparedStatement, resultSet);
        }

    }


    @Override
    public void update(String memberId, int money){ // throws SQLException {
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
            throw new MyDbException(e); // 1. 감싸자.
        }
        finally {
            close(connection, preparedStatement, null);
        }

    }

    @Override
    public void delete(String memberId){ // throws SQLException {
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
            throw new MyDbException(e); // 1. 감싸자.
        }
        finally {
            close(connection, preparedStatement, null);
        }
    }

    private void close(Connection con, Statement statement, ResultSet resultSet){

        JdbcUtils.closeResultSet(resultSet);
        JdbcUtils.closeStatement(statement);
        DataSourceUtils.releaseConnection(con, dataSource);
    }

    private Connection getConnection(){ // throws SQLException {
        Connection connection = DataSourceUtils.getConnection(dataSource);
        log.info("# Get connection={}, class={} v1", connection, connection.getClass());
        return connection;
    }
}
