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

    private final DataSource dataSource; // Connection을 받아오기 위한 DataSource를 받아옴
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        // 트랜잭션은 비즈니스 로직이 있는 서비스 계층에서 시작해야한다. -> 비즈니스 로직이 잘못되면 해당 비즈니스 로직으로 인해 문제가 되는 부분을 함꼐 롤백 해야하기 때문이다.
        // transaction 시작 -> 하나의 세션(커넥션)을 써야겠지??

        Connection connection = dataSource.getConnection();
        try {
            connection.setAutoCommit(false); // -> 트랜잭션 시작

            // 비즈니스 로직 수행
            bizLogic(connection, fromId, toId, money);

            connection.commit(); // 트랜젝션 커밋 -> 커밋 명령어가 connection을 통해서 DB 세션에 전달되고 세션이 commit을 실행하게됨
        }
        catch (Exception e){
            // 트래잭션 실행 중 예외가 발생? -> 롤백
            connection.rollback();
            throw new IllegalStateException(e);
        }
        finally { // commit이든 rollback이든 끝난 상황
            release(connection);
        }



        // transaction commit, rollback
    }

    private void bizLogic(Connection connection, String fromId, String toId, int money) throws SQLException { // 순수 비즈니스 로직만 추출 후 분리
        Member fromMember = memberRepository.findById(connection, fromId);
        Member toMember = memberRepository.findById(connection, toId);

        memberRepository.update(connection, fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(connection, toId, toMember.getMoney() + money);
    }

    private static void release(Connection connection) {
        if(connection != null){ // -> 내가 시작한 connection 내가 끝낸다.
            try{
                //connection.close(); 할건데 지금은 auto commit이 false로 되어있지 -> 그대로 close하면 false인채로 connection pool로 들어감 주의!
                connection.setAutoCommit(true); // 커넥션풀을 고려하여 디폴트로 되돌려 놓기
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
