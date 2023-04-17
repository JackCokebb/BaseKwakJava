package tobyspring.config;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class MyAutoConfigImportSelector implements DeferredImportSelector {//, BeanClassLoaderAware {
    // 6. class loader를 생성자를 통해 빈으로 주입하는 방식이 있음 (요즘 방식)
    private final ClassLoader classLoader;
    public MyAutoConfigImportSelector(ClassLoader classLoader){
        this.classLoader = classLoader;
    }

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        /*
        3. 어떤 파일에서 자동 구성  대상 config의 후보들을 읽어오는
            왜 후보? : 파일에 어떤 config를 넣었다고 해서 무조건 모든 application이 이걸 다 구성정보로 쓰는 것이 아님
                      후보로 잡는 것임. 이 중에서 어떤 것을 사용되게 할 것인지는 스마트한 방법으로 결정할 예정

         */
//        return new String[]{
//                "tobyspring.config.autoConfig.DispatcherServletConfig",
//                "tobyspring.config.autoConfig.TomcatWebServerConfig",
//        };

        // 4. annotation의 class이름을 넣음, 파일 이름이 아니라. && classLoader 필요
        // classloader : application에서 어떤 파일을 읽어올떄 사용함
        //  spring container가 bean을 생성하기 위해서 빈 클래스를 로딩할때 사용하는 그 class loader임
        // 7. ImportCandidates.load()는 import candidate들을  리턴함 (String의 iterable 타입임)
            // 파일에 설정해둔 자동 구성에 사용할 configuration  class의 목록들이 들어있음
        ImportCandidates importCandidates = ImportCandidates.load(MyAutoConfiguration.class, classLoader);

        // 8. Sring[]로 리턴해야되는데 iterable 타입은 iterate할때까지는 사이즈는 알 수 없어서 일단 list에 넣고보자
        List<String> autoConfigs = new ArrayList<>();
//        for (String candidate : importCandidates) {
//            autoConfigs.add(candidate);
//        }

//        importCandidates.forEach(candidate -> autoConfigs.add(candidate));
        importCandidates.forEach(autoConfigs::add);

        // 9. toArray()는 뒤에 넉넉한 사이즈의 배열을 주면 안에 collection 값을 복사하고,
        //      작으면 걔를 무시하고 새로운 배열을 만들어서 그 안에 값을 복사해서 리턴해줌
        //      지금은 어차피 사이즈를 모르니까 비어있는 배열을 넣어주자
        return autoConfigs.toArray(new String[0]);

        //return autoConfigs.stream().toArray(String[]::new); -> java 8부터 가능
        //return Arrays.copyOf(autoConfigs.toArray(), autoConfigs.size(), String[].class); -> 이것도 가능
        //return StreamSupport.stream(importCandidates.spliterator(), false).toArray(String[]::new); -> list 만들것도 없이 이것도 가능
    }

    // 5. BeanClassLoaderAware 인터페이스를 구현하면 스프링 컨테이너가 빈을 로딩할 떄 사용하는 클래스 로더를 주입해줌
//    @Override
//    public void setBeanClassLoader(ClassLoader classLoader) {
//
//    }
}

// 9. 그래서 이 파일을 어디서 로드해 오는데?! -> load()의 설명을 읽고 만들어보자 resources.META-INF.spring.tobyspring.config.MyAutoConfiguration.imports
    // 설명 : The names of the import candidates are stored in files named META-INF/spring/full-qualified-annotation-name.imports on the classpath.

// 10. ImportCandidates.load(MyAutoConfiguration.class, classLoader); 안에 있는 애너테이션 이름(MyAutoConfiguration + .imports)과 동일한 파일에서
//      목록을 읽어와서 string array로 만들고 리턴하는 작업인거 -> 그걸 빈으로 만들고