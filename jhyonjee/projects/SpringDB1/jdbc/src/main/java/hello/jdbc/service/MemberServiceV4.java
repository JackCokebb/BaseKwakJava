package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


/**
 * 예외 누수 문제 해결
 * SQLException 제거
 *
 * MemberRepository 인터페이스 의존
 * 이제 거의 순수 자바 코드만 남을 것
 */

@Slf4j
public class MemberServiceV4 {

    private final MemberRepository memberRepository; // 1. repository 인터페이스로 교체

    public MemberServiceV4(MemberRepository memberRepository) { // 2. 주입도 인터페이스로
        this.memberRepository = memberRepository;

    }

    @Transactional
    public void accountTransfer(String fromId, String toId, int money) { //throws SQLException { -> 3. 예외 명시 제거
            bizLogic(fromId, toId, money);
    }

    private void bizLogic(String fromId, String toId, int money) {  //throws SQLException { -> 3. 예외 명시 제거
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private static void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("이체중 예외");
        }
    }
}
