package tobyspring.helloboot;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.*; // Assertions.assertThat() -> assertThat()로 쓸 수 있음

// JUnit5를 이용한 테스트
public class HelloApiTest {
    @Test
    void helloApi(){ // API 기능 테스트
        // <HTTPie> http localhost:8080/hello?name=Spring
        TestRestTemplate rest = new TestRestTemplate(); //API 요청을 코드를 작성할떄 사용-> 응답을 가져와서 쓸수있음(단, 그냥 RestTemplate을 사용하면 요청 에러에 대한 예외가 throw되면 핸들하기 어려움)

        ResponseEntity<String> res = rest.getForEntity( // ResponseEntity: 웹 응답의 모든걸 가지고 있는 오브젝트, <String>의 String은 body의 타입임
                "http://localhost:8080/hello?name={name}",
                String.class, // response body binding (어떤 자바 오브젝트로 치환할 것인지)
                "Spring" // {name}에 들어갈 실제 파라미터
                );

        // 응답 검증
        // 1. status == 200 ?
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        // 2. header (content-type) == text/plain ?
        assertThat(res.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE)).startsWith(MediaType.TEXT_PLAIN_VALUE);   //.isEqualTo(MediaType.TEXT_PLAIN_VALUE); 안됨// expected: "text/plain" but was: "text/plain;charset=ISO-8859-1"
        // 3. body == Hello Spring ?
        assertThat(res.getBody()).isEqualTo("Hello Spring");
    }

    @Test
    void failsHelloApi(){ // API 기능 테스트
        // <HTTPie> http localhost:8080/hello?name=Spring
        TestRestTemplate rest = new TestRestTemplate(); //API 요청을 코드를 작성할떄 사용-> 응답을 가져와서 쓸수있음(단, 그냥 RestTemplate을 사용하면 요청 에러에 대한 예외가 throw되면 핸들하기 어려움)

        ResponseEntity<String> res = rest.getForEntity( // ResponseEntity: 웹 응답의 모든걸 가지고 있는 오브젝트, <String>의 String은 body의 타입임
                "http://localhost:8080/hello?name=",
                String.class // response body binding (어떤 자바 오브젝트로 치환할 것인지)

        );

        // 응답 검증
        // 1. status == 500 ?
        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
