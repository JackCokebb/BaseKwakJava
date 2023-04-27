package hello.jdbc.repository.ex;

public class MyDbException extends RuntimeException { // Runtime exception -> uncheck exception
    public MyDbException() {
    }

    public MyDbException(String message) {
        super(message);
    }

    public MyDbException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyDbException(Throwable cause) {
        super(cause);
    }
}
