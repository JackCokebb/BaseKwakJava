package tobyspring.study;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationTest {
    @Test
    void configuration(){

        MyConfig myConfig = new MyConfig();
        Bean1 bean1 = myConfig.bean1();
        Bean2 bean2 = myConfig.bean2();
        assertThat(bean1.common).isNotSameAs(bean2.common);

        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
        ac.register(MyConfig.class);
        ac.refresh();
        Bean1 b1 = ac.getBean(Bean1.class);
        Bean2 b2 = ac.getBean(Bean2.class);
        assertThat(b1.common).isSameAs(b2.common);

    }

    @Test
    void proxyCommonMethod(){
        MyConfigProxy myConfigProxy = new MyConfigProxy();
        Bean1 bean1 = myConfigProxy.bean1();
        Bean2 bean2 = myConfigProxy.bean2();

        assertThat(bean1.common).isSameAs(bean2.common);
    }

    static class MyConfigProxy extends MyConfig{
        private Common common;

        @Override
        Common common() {
            if(this.common == null) this.common = super.common();
            return this.common;
        }
    }

    /* proxyBeanMethods = false
    기본 설정은 true -> 컨테이너에 등록된 빈 사용시 프록시를 통해 새로운 객체 생성 하지 않고 기존 객체 가져옴
    기본 설정 false -> 요청마다 새로운 객체 생성
     */
    @Configuration(proxyBeanMethods = true)
    static class MyConfig{
        @Bean
        Common common(){
            return new Common();
        }

        @Bean
        Bean1 bean1(){
            return new Bean1(common());
        }

        @Bean
        Bean2 bean2(){
            return new Bean2(common());
        }
    }

    static class Bean1{
        private final Common common;

        public Bean1(Common common) {
            this.common = common;
        }
    }

    static class Bean2{
        private final Common common;

        public Bean2(Common common) {
            this.common = common;
        }
    }

    static class Common{

    }
}
