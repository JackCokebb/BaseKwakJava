package hello.jdbc.repository.ex;

public class MyDuplicateKeyException extends MyDbException{ // Runtime exception 대신 MyDbException를 extends하면 데이터베이스 관련 예외라는 계층을 만들 수 있다.

    public MyDuplicateKeyException() {
    }

    public MyDuplicateKeyException(String message) {
        super(message);
    }

    public MyDuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyDuplicateKeyException(Throwable cause) {
        super(cause);
    }
}
