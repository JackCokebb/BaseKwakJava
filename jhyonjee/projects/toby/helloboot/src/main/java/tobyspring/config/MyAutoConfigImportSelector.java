package tobyspring.config;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class MyAutoConfigImportSelector implements DeferredImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        // 1. Import 할 class 이름을 package까지 포함해서 String array로 만들어서 리턴하면됨
        // 2. 요 안에 있는 것들의 class를 찾아서 구성정보에 추가해줌
        // 3. 이 selectImports는 쓴다고 어떻게 명시할 수 있을까? -> @Import() 안에 클래스로 넣어주면 됨 -> @EnableMyAutoConfiguration으로 이동
            // 이걸 이용하면 우리가 사용할 config class를 자유롭게 선택하게 만들 수 있음(아직은 하드코딩으로 박아넣어서 자유롭진 않음)
        return new String[]{
                "tobyspring.config.autoConfig.DispatcherServletConfig",
                "tobyspring.config.autoConfig.TomcatWebServerConfig",
        };
    }
}
