package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

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
            preparedStatement.executeUpdate(); // 쿼리가 DB에 실행됨 -> DB에 영향받은 row 수만큼 return함 // DB에 변결할때 쓰는 executeUpdate

            return member;
        } catch (SQLException e) {
            log.error("# DB error");
            throw e;
        }
        finally {
            // close는 항상 호출되는 것을 보장하도록 finally에서 실행
            close(con, preparedStatement, null);

        }
    }

    public Member findById(String memberId) throws SQLException {
        String sql = "SELECT * FROM MEMBER WHERE member_id = ?";

        Connection connection = null; // try-catch에서도 이 구분을 써야하기 떄문에 밖으로 빼줌
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try{
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, memberId);

            resultSet = preparedStatement.executeQuery(); // select문 쓸때는 executeQuery 사용 -> result set(select의 결과를 담고 있음) 반환
            if(resultSet.next()){ // resultSet에 한번은 next를 해줘야 실제 데이터가 있는 시작점이 나옴
                Member member = new Member();
                member.setMemberId(resultSet.getString("member_id"));
                member.setMoney(resultSet.getInt("money"));
                return member;
            }
            else{ //resultSet.next()가 false라는 것은 data가 없다는 뜻
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

    private void close(Connection con, Statement statement, ResultSet resultSet){
        // 생성 역순으로 close
        // 안닫으면 외부 리소스(tcp/ip)를 쓰는 중인데 계속 연결이 유지되어 버림 - 리소스

        // 코드의 안정성을 위해 if 문으로 감싸기
        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                log.error("# statement close error");
            }
        }
        if(statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                log.error("# statement close error");
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

    private static Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }
}
