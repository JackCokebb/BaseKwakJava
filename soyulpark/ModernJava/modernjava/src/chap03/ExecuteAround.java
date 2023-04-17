package chap03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ExecuteAround {

    private static final String FILE = ExecuteAround.class.getResource("./data.txt").getFile();

    public static void main(String... args) throws IOException {
        // 더 유연하게 리팩토링할 메서드
        String result = processFileLimited();
        System.out.println(result);

        System.out.println("---");

        // Process 과정에서 1줄씩 읽는 중
        String oneLine = processFile((BufferedReader b) -> b.readLine());
        System.out.println(oneLine);

        // Process 과정에서 여러 줄 읽기 가능
        // process 메서드로 람다를 이용하여 동작을 전달
        String twoLines = processFile((BufferedReader b) -> b.readLine() + b.readLine());
        System.out.println(twoLines);
    }

    public static String processFileLimited() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            return br.readLine();
        }
    }


    // 이전 코드
    public static String processFile1(BufferedReaderProcessor p) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            return br.readLine();
        }
    }


    // 함수형 인터페이스 생성
    @FunctionalInterface
    public interface BufferedReaderProcessor {
        String process(BufferedReader b) throws IOException;

    }

    // 인터페이스를 메서드의 인자로 전달
    public static String processFile(BufferedReaderProcessor p) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            return p.process(br);           // BufferedReader 객체 처리
        }
    }
}
