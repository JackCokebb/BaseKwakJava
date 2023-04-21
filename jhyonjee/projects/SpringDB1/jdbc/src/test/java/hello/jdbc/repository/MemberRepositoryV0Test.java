package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 memberRepositoryV0 = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        // save
        Member member = new Member("memberV7", 10000);
        memberRepositoryV0.save(member);

        // findById
        Member findMember = memberRepositoryV0.findById(member.getMemberId());
        log.info("# find member = {}", findMember);
        log.info("# find member == member  {}", findMember == member); // false
        log.info("# find member .equals member  {}", findMember.equals(member)); // true

        // update
        memberRepositoryV0.update(member.getMemberId(), 20000);
        Member updatedMember = memberRepositoryV0.findById(member.getMemberId());
        assertThat(updatedMember.getMoney()).isEqualTo(20000);

        // delete
        memberRepositoryV0.delete(member.getMemberId());
        assertThatThrownBy(()-> memberRepositoryV0.findById(member.getMemberId()))  // assertThatThrownBy() -> Exception을 받아옴
                .isInstanceOf(NoSuchElementException.class);



        assertThat(findMember).isEqualTo(member);
    }
}