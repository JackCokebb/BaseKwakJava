package hello.jdbc.exception.translator;

import hello.jdbc.connection.ConnectionConst;
import hello.jdbc.domain.Member;
import hello.jdbc.repository.ex.MyDbException;
import hello.jdbc.repository.ex.MyDuplicateKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ExTranslatorV1Test {

    Repository repository;
    Service service;

    @BeforeEach
    void init(){ // 5. init
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        repository = new Repository(dataSource);
        service = new Service(repository);
    }

    @Test
    void duplicateKeySave(){
        service.create("myId");
        service.create("myId"); // 6. 같은 ID 저장 시도.
    }

    @Slf4j
    @RequiredArgsConstructor
    static class Service{
        private final Repository repository;

        public void create(String memberId){ // 회원가입 로직
            try{
                repository.save(new Member(memberId, 0)); // 3. MyDuplicateKeyException 터질 수 있음. -> try catch 적용
                log.info("# savedID={}", memberId);
            }
            catch(MyDuplicateKeyException e){
                log.info("# 키 중복, 복구 시도");
                String retryId = generateNewId(memberId);
                log.info("# retryId={}", retryId);
                repository.save(new Member(retryId, 0));
            }
            catch (MyDbException e){ // 어차피 runtime exception이라 catch문 없어도 밖으러 던져짐
                log.info("# 데이터 접근 계층 예외 ", e);
                throw e;
            }

        }
        private String generateNewId(String membberId){ // 4. 새로운 아이디 생성 로직 (MyDuplivateKeyException 터졌을때 실행)
            return membberId + new Random().nextInt(10000);
        }
    }

    @RequiredArgsConstructor
    static class Repository{
        private final DataSource dataSource;
        public Member save(Member member){
            String sql = "insert into member(member_id, money) values(?,?)";
            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try{
                connection = dataSource.getConnection();
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, member.getMemberId());
                preparedStatement.setInt(2, member.getMoney());
                preparedStatement.executeUpdate();
                return member;
            }
            catch (SQLException e){ // 1. SQLException을 더이상 밖으로 던지지 않을거야
                //h2 db 기준
                if(e.getErrorCode() == 23505){ // pk 중복인 경우
                    throw new MyDuplicateKeyException(e); // 2. 감싸서 던질거야 -> 서비스에서 잡아서 복구 가능
                }
                throw new MyDbException(e);
            }
            finally{
                JdbcUtils.closeStatement(preparedStatement);
                JdbcUtils.closeConnection(connection);

            }
        }

    }
}
