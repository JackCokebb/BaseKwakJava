package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

// 밑에 거를 직접 가져다 쓰고 있다는 것이 문제!!
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 트랜잭션 매니저
 */

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV3_1 {

//    private final DataSource dataSource; -> 이걸 직접 쓰면 안됨! -> 의존하고 있잖아 단일 책임 원칙 위배
    private final PlatformTransactionManager transactionManager; // 1.
    private final MemberRepositoryV3 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        //Connection connection = dataSource.getConnection();

        // 트랜잭션 시작
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition()); // -> 트랜잭션 시작 2.

        try {
            //connection.setAutoCommit(false); // -> 트랜잭션 시작 (JDBC 트랜잭션에 의존) -> 만약 JPA를 쓰는것으로 바꾸면?? 이 코드 박살나겠지?
            bizLogic(fromId, toId, money);

            //connection.commit();
            transactionManager.commit(transactionStatus); // 성공시 커밋
        }
        catch (Exception e){
            //connection.rollback();
            transactionManager.rollback(transactionStatus);
            throw new IllegalStateException(e);
        }
//        finally {   // -> commit이나 rollback 될때 transactionManager가 알아서 release해줌 -> transaction이 종료되니 connection을 쓸일이 없음
//            release(connection);
//        }
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException { // -> 3. 이제 connection을 파라미터로 넘겨줄 필요 없제 -> 제거
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
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
