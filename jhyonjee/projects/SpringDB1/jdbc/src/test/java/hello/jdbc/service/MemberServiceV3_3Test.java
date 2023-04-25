package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 트랜잭션 - @Transactional AOP
 */
@Slf4j
@SpringBootTest // 4. 이걸 붙여주면 spring이 보고 test를 돌릴때 spring을 하나 띄움 -> 필요한 spring bean을 등록 및 의존관계 주입
class MemberServiceV3_3Test {

    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    @Autowired // 5. 의존관계 주입 -> 근데 애네를 빈으로 등록을 안했는데 어케 주입함?
    private MemberRepositoryV3 memberRepository;
    @Autowired // 9. autowired로 얘를 빈으로 주입했지만 사실 프록시가 빈으로 등록되고 의존관계 주입을 받게되고 걔가 먼저 호출됨 -> 프록시 안에서도 실제 서비스 로직은 프록시 말고 실제 서비스 빈을 프록시가 호출함
    private MemberServiceV3_3 memberService;

    @TestConfiguration // 7. bean으로 등록해주자 -> Transaction AOP를 쓰기 위해 필요한 녀석 -> spring boot에서 등록하는 빈 말고 추가로 내가 넣을 빈 추가
    static class TestConfig{
        @Bean
        DataSource dataSource(){
            return new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        }

        @Bean
        PlatformTransactionManager transactionManager(){ // -> AOP 프록시가 얘를 주입받아서 쓸 것
            return new DataSourceTransactionManager(dataSource());
        }

        @Bean
        MemberRepositoryV3 memberRepositoryV3(){
            return new MemberRepositoryV3(dataSource());

        }
        @Bean
        MemberServiceV3_3 memberServiceV3_3(){
            return new MemberServiceV3_3(memberRepositoryV3());
        }
    }

    /* 6. 주석처리 -> 여기 있던 애들 bean으로 등록해야함
    @BeforeEach
    void before(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
//        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource); --> 1. @Transactional 에서 생기는 Proxy가 트랜잭션을 다 처리하기 때문
        // 2. 근데 rollback 안될껄? 왜?
        // 밑에 repository랑 service를 직접 생성하여 썼음 -> bean으로 등록한 것이 아님 -> spring을 사용한 것이 아님
        // 3. Transaction AOP를 적용하(쓸)려면 spring AOP 등 spring이 제공하는게 다 제공되어야함 -> 즉 spring container에 spring bean을 등록해야함
        //      그래야 spring이 그걸 보고 안에서 원하는 것을 만들 수 있음
        // 4. spring bean으로 등록해보자!
        memberRepository = new MemberRepositoryV3(dataSource);
        memberService = new MemberServiceV3_3(memberRepository);
    }*/

    @AfterEach // 각각의 test가 끝날때 호출된다.
    void after() throws SQLException {
        memberRepository.delete(MEMBER_A);
        memberRepository.delete(MEMBER_B);
        memberRepository.delete(MEMBER_EX);
    }

    @Test
    void AopCheck(){ // 8.
        log.info("# MemberService class={}", memberService.getClass());
        // 결과->  # MemberService class=class hello.jdbc.service.MemberServiceV3_3$$EnhancerBySpringCGLIB$$b240e3df
        //          $$EnhancerBySpringCGLIB$$b240e3df 이게 뭐여 -> proxy가 @Transactional이 붙은 memberService를 상속받아서 오버라이드해서 만든 트랜잭션 프록시

        log.info("# MemberService class={}", memberRepository.getClass());
        Assertions.assertThat(AopUtils.isAopProxy(memberService)).isTrue();
        Assertions.assertThat(AopUtils.isAopProxy(memberRepository)).isFalse();
    }

    @Test
    @DisplayName("정상 이체")
    void accountTransfer() throws SQLException {
        // given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        // when
        log.info("# Start TX");
        // connection이 파라미터로 명시되고 따라서 같은 connection을 사용하게됨 -> 트랜잭션 유지 가능!
        memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);
        log.info("# End TX");

        // then
        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberB = memberRepository.findById(memberB.getMemberId());
        assertThat(findMemberA.getMoney()).isEqualTo(8000);
        assertThat(findMemberB.getMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("이체중 예외 발생")
    void accountTransferEx() throws SQLException {
        // given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberEx = new Member(MEMBER_EX, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberEx);

        // when
        assertThatThrownBy(()-> memberService.accountTransfer(memberA.getMemberId(), memberEx.getMemberId(), 2000))
                .isInstanceOf(IllegalStateException.class);


        // then
        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberB = memberRepository.findById(memberEx.getMemberId());

            // 예외가 발생해서 실패했지만 serviceV2에서 예외를 캐치해서 롤백해주었음.
        assertThat(findMemberA.getMoney()).isEqualTo(10000);
        assertThat(findMemberB.getMoney()).isEqualTo(10000);
    }

}