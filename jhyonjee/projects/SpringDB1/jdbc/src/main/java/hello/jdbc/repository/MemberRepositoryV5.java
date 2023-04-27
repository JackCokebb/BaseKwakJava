package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import javax.sql.DataSource;

/**
 * JdbcTempalte 사용 - 리포지토리에서 JDBC를 사용하는 반복 코드가 제거됨.
 */
@Slf4j
public class MemberRepositoryV5 implements MemberRepository{

    private final JdbcTemplate jdbcTemplate;
    public MemberRepositoryV5(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member){
        String sql = "INSERT INTO member(member_id, money) VALUES (?, ?)";
        int updatedRows = jdbcTemplate.update(sql, member.getMemberId(), member.getMoney());
        return member;
    }

    @Override
    public Member findById(String memberId){
        String sql = "SELECT * FROM MEMBER WHERE member_id = ?";

        Member member = jdbcTemplate.queryForObject(sql, memberRowMapper(), memberId);
        return member;
    }

    private RowMapper<Member> memberRowMapper() {
        return (resultSet, rowNum) -> {
            Member member = new Member();
            member.setMemberId(resultSet.getString("member_id"));
            member.setMoney(resultSet.getInt("money"));
            return member;
        };
    }


    @Override
    public void update(String memberId, int money){
        String sql = "UPDATE member SET money=? WHERE member_id=?";
        jdbcTemplate.update(sql, money, memberId);
    }

    @Override
    public void delete(String memberId){
        String sql = "DELETE FROM member WHERE member_id=?";
        jdbcTemplate.update(sql,memberId);
    }
}
