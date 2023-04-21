package hello.jdbc.connection;

/**
 * connection 상수
 */
public abstract class ConnectionConst { // 상수를 모아두었기 때문에 객체가 생성되면 안되겠지? -> abstract으로 객체 생성 막아둠
    public static final String URL = "jdbc:h2:tcp://localhost/~/test";
    public static final String USERNAME = "sa";
    public static final String PASSWORD = "";
}
