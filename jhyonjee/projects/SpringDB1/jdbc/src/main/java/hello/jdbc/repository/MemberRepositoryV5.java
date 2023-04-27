package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JdbcTempalte 사용
 */
@Slf4j
public class MemberRepositoryV5 implements MemberRepository{

    private final JdbcTemplate jdbcTemplate;
    public MemberRepositoryV5(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member){
        String sql = "INSERT INTO member(member_id, money) VALUES (?, ?)";
        int updatedRows = jdbcTemplate.update(sql, member.getMemberId(), member.getMoney());// preparedStatement.executeUpdate(); 여서 update임. -> update된 row 수 반환
        // member.getMemberId(), member.getMoney()는 각 ?에 들어가는 파라미터.
        return member;


//          JdbcTemplate이 다 해주기 때문에 JdbcTemplate으로 밑에 중복되는 코드 제거 가능!(connection 받는 거부터 ~ 예외 변환까지)
//        Connection con = null;
//        PreparedStatement preparedStatement = null;
//
//
//        try {
//            con = getConnection();
//            preparedStatement = con.prepareStatement(sql);
//
//            preparedStatement.setString(1, member.getMemberId());
//            preparedStatement.setInt(2, member.getMoney());
//            preparedStatement.executeUpdate();
//
//            return member;
//        } catch (SQLException e) {
//            throw exTranslator.translate("save", sql, e);
//        }
//        finally {
//            close(con, preparedStatement, null);
//
//        }
    }

    @Override
    public Member findById(String memberId){
        String sql = "SELECT * FROM MEMBER WHERE member_id = ?";

        // 한 건만 조회하는 것은 queryForObject
        Member member = jdbcTemplate.queryForObject(sql, memberRowMapper(), memberId);// memberRowMapper: query 결과를 어떻게 매핑할 것인지에 대한 정보
        return member;

        /*Connection connection = null;
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
            throw exTranslator.translate("findById", sql, e);
        }
        finally {
            close(connection, preparedStatement, resultSet);
        }*/

    }

    private RowMapper<Member> memberRowMapper() {
        return (resultSet, rowNum) -> {
            Member member = new Member();
            member.setMemberId(resultSet.getString("member_id"));
            member.setMoney(resultSet.getInt("money"));
            return member;
        };
    }


    @Override
    public void update(String memberId, int money){
        String sql = "UPDATE member SET money=? WHERE member_id=?";
        jdbcTemplate.update(sql, money, memberId);


        /*Connection connection = null;
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
            throw exTranslator.translate("update", sql, e);
        }
        finally {
            close(connection, preparedStatement, null);
        }*/
    }

    @Override
    public void delete(String memberId){
        String sql = "DELETE FROM member WHERE member_id=?";
        jdbcTemplate.update(sql,memberId);

        /*Connection connection = null;
        PreparedStatement preparedStatement = null;

        try{
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, memberId);

            int resultSize = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            log.error("# prepareStatement ERROR - delete()");
            throw exTranslator.translate("delete", sql, e);
            //throw new MyDbException(e);
        }
        finally {
            close(connection, preparedStatement, null);
        }*/
    }

    /*
    // Connection 열고 닫는것도 JdbcTemplate에서 다 해줌!!
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
    */
}
