package tobyspring.study;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class ConfigurationTest {
    /**
     * configuration의 default 구성과 특징을 알아보자
     * configuration class의 특징:
     * @Bean이 붙은 메소드들이 많고 각각이 자바 코드에 의해 bean object를 직접 생성하고 다른 오브젝트와 관계를 설정함
     *
     */
    @Test
    void configuration(){
        // 1. 에시
        /* Bean1 <-- Common (Bean1 오브젝트가 Common 오브젝트에 의존한다는 뜻)
           Bean2 <-- Common
           싱글톤이기 때문에 Bean1과 Bean2가 의존하고 있는 Common은 동일한 오브젝트여야 한다 -> 빈 오브젝트를 생성해서 동일하게 주입해야된다는 뜻
           근데 자바코드로 팩토리 메소드를 만들어서 각 빈을 생성하는 메소드들을 상호 호출하는 방식으론 의존관계를 주입하면 싱긅톤 룰을 지킬 수 없음
        */

        //3. 테스트 코드를 짜보자
        //Assertions.assertThat(new Common()).isSameAs(new Common()); //isSameAs() -> 객체의 주소값까지 비교해줌
        //MyConfig myConfig = new MyConfig();
        //Bean1 bean1 = myConfig.bean1();
        //Bean2 bean2 = myConfig.bean2();
        //Assertions.assertThat(bean1.common).isSameAs(bean2.common); // 딩연히 다르다고 나옴

        /*
        4. 근데 MyConfig class를 spring container의 구성정보로 사용하게되면 common 오브젝트를 주입하는 방식 등 동작하는 방식이 달라짐
           분명히 MyConfig에 있는 Bean1, Bean2는 Common의 팩토리 메소드를 각각 사용해서 common을 주입했는데 common의 객체가 같다고 나옴
         */
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
        annotationConfigApplicationContext.register(MyConfig.class); // @Configuration이 붙은 클래스를 부트 스트랩핑하는 기본 빈으로 등록
        annotationConfigApplicationContext.refresh(); // 초기화

        Bean1 bean1 = annotationConfigApplicationContext.getBean(Bean1.class);
        Bean2 bean2 = annotationConfigApplicationContext.getBean(Bean2.class);
        // 분명히 MyConfig에 있는 Bean1, Bean2는 Common의 팩토리 메소드를 각각 사용해서 common을 주입했는데 common의 객체가 같다고 나옴
        Assertions.assertThat(bean1.common).isSameAs(bean2.common);
        /*
        5. @Configuration이 붙은 클래스가 spring container에서 사용되어 질때 발생하는 마법임
           기본적으로 @Configuration의 proxy bean method가 true(default)로 설정되어 있는 경우에는
           MyConfig이라는 클래스가 bean으로 등록될때 직접 bean으로 등록되는 것이 아니라 proxy object를 앞에 하나 두고
           그것이 bean으로 등록이 됨
         */
    }

    // 8. 이 프록시를 이용해서 @Configuration이 붙은 클래스가 스프링의 빈으로 사용되어질 때 보였던 그 독튻한 동작방식을 흉내낼 수 있다.
    void proxyCommonMethod(){
        // 9.이 프록시는 decorator처럼 타겟 오브젝트를 따로  두고 자기가 동적으로 끼어들어서
        //   중간에 중개하는 역할을 하느게 아니라 애초에 이걸 확장해서 타겟을 대체하는 방식으로 동작함
        MyConfigProxy myConfigProxy = new MyConfigProxy();
        Bean1 bean1 = myConfigProxy.bean1();
        Bean2 bean2 = myConfigProxy.bean2();

        // 10. 두 개의 빈에 들어있는 common 오브젝트가 동일한지 체크
        //    spring container 내부에서 spring container의 도움을 받은 것은 아니지만 직접 proxy를 만들어서 spring container 내부에서 일어나는 일을 흉내내본 것
        //    이렇게 하면 확장한 이 프록시 오브젝트를 통해서 common method가 생성하는 common object를 하나로 제한하고 하나로 재사용할 수 있게 캐싱하는 방삭으로 먼둘 수 있다.
        Assertions.assertThat(bean1.common).isSameAs(bean2.common) ;

        // 11. Configuration class에 proxybean method가 true로 들어가 있다는 것은 처음에 spring container가 시작할때 이 프록시 class(MyConfigProxy 같은)를 생성을하고
        //     @Configuration annotation이 붙은 빈 오브젝트로 사용하는 것이다 -> 따라서 오브젝트를 여러번 생성하게끔 호출을 하다고 하더라도 딱 한개의 오브젝트만 생성되게
    }

    // 6. 어떤 식으로 proxy가 만들어지는지 보자
    static class MyConfigProxy extends MyConfig{
        private Common common;
        @Override
        Common common() {
            //Common common = super.common();
            // 바로 생성해서 주는 것이 아닌 없으면 새로 만들어 주기
            // common 필드가 null이 아니고 있으면(캐싱 해두었으면) 캐싱해둔거 줌 (재사용)
            if (this.common == null) this.common = super.common();

            return this.common;
        }
        // 7. MyConfig를 확장해서 타겟 오브젝트에 대한 접근 방식을 제어하는 프록시를 만든거임
    }

    @Configuration
    static class MyConfig{
        @Bean
        Common common(){
            return new Common();
        }

        @Bean
        Bean1 bean1(){
            return new Bean1(common()); // common을 생성하는 팩토리 메소드로 주입
        }

        @Bean
        Bean2 bean2(){
            return new Bean2(common());
        }
        /*
        2. 평범한 자바코드라고 생각하고 bean1(), bean2()를 실행하면 common은 몇번 생성이 될까? -> 2번(서로 다른 2개의 common)
        3. 테스트 코드를 짜보자
         */
    }
    static class Bean1 {
        private final Common common;

        Bean1(Common common){
            this.common = common;
        }

    }

    static class Bean2 {
        private final Common common;

        Bean2(Common common){
            this.common = common;
        }

    }

    static class Common{

    }


}
