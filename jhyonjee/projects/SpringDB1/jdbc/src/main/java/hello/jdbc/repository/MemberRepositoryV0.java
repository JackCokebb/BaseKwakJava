package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 *  JDBC - DriverManager 사용 (low level)
 */
@Slf4j
public class MemberRepositoryV0 {
    public Member save(Member member) throws SQLException {
        String sql = "INSERT INTO member(member_id, money) VALUES (?, ?)"; // 데이터 등록

        Connection con = null;
        PreparedStatement preparedStatement = null; // 이걸로 데이터베이스에 쿼리를 날리는 것 - 데이터로 전달한 SQL과 파라미터로 전달할 데이터들을 준비


        try {
            con = getConnection();
            preparedStatement = con.prepareStatement(sql);

            //sql 변수에 넣어줬던 동적 파라미터 지정 - 타입 주의!
            preparedStatement.setString(1, member.getMemberId());
            preparedStatement.setInt(2, member.getMoney());
            preparedStatement.executeUpdate(); // 쿼리가 DB에 실행됨 -> DB에 영향받은 row 수만큼 return함

            return member;
        } catch (SQLException e) {
            log.error("# DB error");
            throw e;
        }
        finally {
            // 생성 역순으로 close
            // 안닫으면 외부 리소스(tcp/ip)를 쓰는 중인데 계속 연결이 유지되어 버림 - 리소스
            // close는 항상 호출되는 것을 보장하도록 finally에서 실행
            close(con, preparedStatement, null);

        }
    }
    private void close(Connection con, Statement statement, ResultSet resultSet){  // Statement : sql을 그대로 넣는 것, PreparedStatement : parameter를 binding할 수 있는 것
        // 코드의 안정성을 위해 if 문으로 감싸기


        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                log.error("# statement close error");
            }
        }
        // close를 하는 과정에서 exception이 터져버리면 다음 코드는 실행이 안됨 -> connection.close가 안될 수 있음
        // try - catch로 잡아서 버리자
        if(statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                log.error("# statement close error"); // 닫을때 예외가 발생한 것이라 틑ㄱ별히 해줄 수 있는 것이 없음
            }
        }

        if(con != null){
            try {
                con.close();
            } catch (SQLException e) {
                log.error("# connection close error");
            }
        }

    }

    private static Connection getConnection() { // 여러번 쓸거 같아서 빼둠
        return DBConnectionUtil.getConnection();
    }
}
