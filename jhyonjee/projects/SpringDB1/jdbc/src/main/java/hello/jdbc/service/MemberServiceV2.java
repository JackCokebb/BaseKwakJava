package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 파라미터 연동, 풀을 고려한 종료
 */

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV2 {

    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        Connection connection = dataSource.getConnection();
        try {
            connection.setAutoCommit(false); // -> 트랜잭션 시작 (JDBC 트랜잭션에 의존) -> 만약 JPA를 쓰는것으로 바꾸면?? 이 코드 박살나겠지?

            bizLogic(connection, fromId, toId, money);

            connection.commit();
        }
        catch (Exception e){
            connection.rollback();
            throw new IllegalStateException(e);
        }
        finally {
            release(connection);
        }
    }

    private void bizLogic(Connection connection, String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(connection, fromId);
        Member toMember = memberRepository.findById(connection, toId);

        memberRepository.update(connection, fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(connection, toId, toMember.getMoney() + money);
    }

    private static void release(Connection connection) {
        if(connection != null){
            try{
                connection.setAutoCommit(true);
                connection.close();
            }
            catch (Exception e){
                log.info("# error!", e);
            }
        }
    }

    private static void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체중 예외");
        }
    }
}
