package tobyspring.helloboot;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloApiTest {

    @Test
    void helloApi() {
        TestRestTemplate rest = new TestRestTemplate();

        // url 주소로 요청을 보내고 String 타입으로 응답 받을 것이고, name 변수에 Spring을 넣겠다
        ResponseEntity<String> res = rest.getForEntity("http://localhost:8080/hello?name={name}", String.class, "Spring");

        // 응답 status code가 200 과 일치하는지 확인
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        // 응답 헤더의 contetnt type이 text/plain 으로 시작하는지 확인
        assertThat(res.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE)).startsWith(MediaType.TEXT_PLAIN_VALUE);
        // 응답 바디가 기대하던 것과 일치하는지 확인
        assertThat(res.getBody()).isEqualTo("Hello Spring");
    }
}
