package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.ex.MyDbException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * SQLExceptionTranslator 추가
 */
@Slf4j
public class MemberRepositoryV4_2 implements MemberRepository{

    private final DataSource dataSource;
    private final SQLExceptionTranslator exTranslator; // 1. SQLExceptionTranslator 인터페이스 추가.

    public MemberRepositoryV4_2(DataSource dataSource) {
        this.dataSource = dataSource;
        this.exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource); // 2. 주입 // dataSource가 필요한 이유 -> 어떤 DB를 쓰는지 같은 정보가 필요해서
    }

    @Override
    public Member save(Member member){
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
            throw exTranslator.translate("save", sql, e); // 3. 예외가 알아서 변환되고, 변환된 예외를 바로 던지기
            //throw new MyDbException(e);
        }
        finally {
            close(con, preparedStatement, null);

        }
    }

    @Override
    public Member findById(String memberId){
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
            throw exTranslator.translate("findById", sql, e); // 3. 예외가 알아서 변환되고, 변환된 예외를 바로 던지기
            //throw new MyDbException(e);
        }
        finally {
            close(connection, preparedStatement, resultSet);
        }

    }


    @Override
    public void update(String memberId, int money){
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
            log.error("# prepareStatement ERROR - update()");
            throw exTranslator.translate("update", sql, e); // 3. 예외가 알아서 변환되고, 변환된 예외를 바로 던지기
            //throw new MyDbException(e);
        }
        finally {
            close(connection, preparedStatement, null);
        }

    }

    @Override
    public void delete(String memberId){
        String sql = "DELETE FROM member WHERE member_id=?";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try{
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, memberId);

            int resultSize = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("# prepareStatement ERROR - delete()");
            throw exTranslator.translate("delete", sql, e); // 3. 예외가 알아서 변환되고, 변환된 예외를 바로 던지기
            //throw new MyDbException(e);
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

    private Connection getConnection(){
        Connection connection = DataSourceUtils.getConnection(dataSource);
        log.info("# Get connection={}, class={} v1", connection, connection.getClass());
        return connection;
    }
}
