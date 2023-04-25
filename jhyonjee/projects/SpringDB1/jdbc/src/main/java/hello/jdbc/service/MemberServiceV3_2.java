package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 트랜잭션 템플릿 (중복되는 코드 제거)
 */

@Slf4j
public class MemberServiceV3_2 {

    //private final PlatformTransactionManager transactionManager;
    private final TransactionTemplate txTemplate; // 1. 템플릿 추가 -> 성공 시 커밋하고 실패시 롤백하는 중복되는 과정을 묶자
    private final MemberRepositoryV3 memberRepository;

    public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryV3 memberRepository) { // 주입
        // TransactionTemplate을 쓰려면 transactionManager가 필요함
        // TransactionTemplate을 빈으로 등록하고 주입해도 됨
        // 그냥 밑에처럼 쓰는 이유는 관례상 + transaction manager를 갈아낄 수 있는 유연성이 생김
        this.txTemplate = new TransactionTemplate(transactionManager);
        this.memberRepository = memberRepository;
    }

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        txTemplate.executeWithoutResult(transactionStatus -> { // executeWithoutResult : 반환값이 없는 경우 -> accountTransfer는 반환값이 없음
            // 비즈니스 로직
            try {
                bizLogic(fromId, toId, money);
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        });

        // transaction template을 적용함으로써 밑의 코드가 필요 없어짐 -> 성공 시 커밋하고 실패시 롤백하는 중복되는 과정을 template으로 묶었음
//        // 트랜잭션 시작
//        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
//
//        try {
//
//            transactionManager.commit(transactionStatus); // 성공시 커밋
//        }
//        catch (Exception e){
//            transactionManager.rollback(transactionStatus); // 실패시 롤백
//            throw new IllegalStateException(e);
//        }
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    // 직접 release하지 않음.
//    private static void release(Connection connection) {
//        if(connection != null){
//            try{
//                connection.setAutoCommit(true);
//                connection.close();
//            }
//            catch (Exception e){
//                log.info("# error!", e);
//            }
//        }
//    }

    private static void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체중 예외");
        }
    }
}
