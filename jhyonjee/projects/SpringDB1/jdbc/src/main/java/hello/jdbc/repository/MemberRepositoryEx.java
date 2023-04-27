package hello.jdbc.repository;
import hello.jdbc.domain.Member;
import java.sql.SQLException;
public interface MemberRepositoryEx {
    Member save(Member member) throws SQLException; // 이 인터페이스의 구현체에서 발생하는 예외를 명시해줘야한다.
    Member findById(String memberId) throws SQLException;
    void update(String memberId, int money) throws SQLException;
    void delete(String memberId) throws SQLException;
}
/*
인터페이스의 구현체가 체크 예외를 던지려면, 인터페이스 메서드에 먼저 체크 예외를 던지는 부분이 선언 되어 있어야 한다.
그래야 구현 클래스의 메서드도 체크 예외를 던질 수 있다. -> 문제점. 이것은 우리가 원하던 순수한 인터페이스가 아니다.
JDBC 기술에 종속적인 인터페이스일 뿐이다.
인터페이스를 만드는 목적은 구현체를 쉽게 변경하기 위함인데, 이미 인터페이스가 특정 구현 기술에 오염이 되어 버렸다.
향후 JDBC가 아닌 다른 기술로 변경한다면 인터페이스 자체를 변경해야 한다.

런타임 예외와 인터페이스를 쓰자!
런타임 예외는 이런 부분에서 자유롭다.
인터페이스에 런타임 예외를 따로 선언하지 않아도 된다.
따라서 인터페이스가 특정 기술에 종속적일 필요가 없다.
 */