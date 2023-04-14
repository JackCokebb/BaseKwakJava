package tobyspring.config;

import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class MyAutoConfigImportSelector implements DeferredImportSelector {

    private final ClassLoader classLoader;

    public MyAutoConfigImportSelector(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        List<String> autoConfigs = new ArrayList<>();

        // 자동 구성에 사용할 Configuration 파일의 목록이 들어감
        /*
        load 메소드가 파일을 읽어오는 위치
        Loads the names of import candidates from the classpath. The names of the import candidates are stored in files named META-INF/spring/full-qualified-annotation-name.imports on the classpath. Every line contains the full qualified name of the candidate class. Comments are supported using the # character.
        Params:
        annotation – annotation to load classLoader – class loader to use for loading
        Returns:
        list of names of annotated classes
         */
        ImportCandidates.load(MyAutoConfiguration.class, classLoader).forEach(autoConfigs::add);

        return autoConfigs.toArray(String[]::new);
    }
}
