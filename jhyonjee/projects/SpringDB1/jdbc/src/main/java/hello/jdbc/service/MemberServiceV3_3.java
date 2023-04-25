package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - @Transactional AOP
 */

@Slf4j
///@Transactional -> class에 붙이면 class 내에 모든 public한 메소드에 @Transactional이 적용됨.
public class MemberServiceV3_3 {

    //private final TransactionTemplate txTemplate;
    private final MemberRepositoryV3 memberRepository;

    public MemberServiceV3_3(MemberRepositoryV3 memberRepository) {
        //this.txTemplate = new TransactionTemplate(transactionManager);  -> 1. transaction 관련 코드 없어도 됨
        this.memberRepository = memberRepository;
    }

    @Transactional // 2. @Transactional 추가 -> 이 메서드가 호출될때 transaction 걸고 시작하겠다,
                   //    트랜잭션이 끝나면, 성공하면 커밋, 예외 터지면 롤백하겠다를 명시 Transaction AOP
                    //메소드나 클래스에 @Transactional이 걸리면 Spring이 스캔할때 이걸 보고 '너는 AOP 적용 대상이구나' 하면서 프록시를 만들어서 적용해줌
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
            bizLogic(fromId, toId, money);    // 비즈니스 로직만 남았군
    }

    private void bizLogic(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    // transaction 관련 코드 제거 -> 필요 없음
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
