package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 memberRepositoryV0 = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        // save
        Member member = new Member("memberV2", 10000);
        memberRepositoryV0.save(member);

        // findById
        Member findMember = memberRepositoryV0.findById(member.getMemberId());
        log.info("# find member = {}", findMember);
        log.info("# find member == member  {}", findMember == member); // false
        log.info("# find member .equals member  {}", findMember.equals(member)); // true
        // -> Member class에 붙어있는 @Data에 equals 메소드가 구현되게끔 되어있고 필드 값이 동일하면 같다고 함, EqualsAndHashCode


        assertThat(findMember).isEqualTo(member); // 애도 내부적으로 Equals를 씀
    }
}