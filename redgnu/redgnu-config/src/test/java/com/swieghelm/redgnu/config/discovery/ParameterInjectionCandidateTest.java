package com.swieghelm.redgnu.config.discovery;

import com.google.inject.TypeLiteral;
import com.swieghelm.redgnu.config.discovery.ParameterInjectionCandidate;
import com.swieghelm.redgnu.config.extension.vanilla.Config;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

class ParameterInjectionCandidateTest {

    @Test
    public void mustProvideForStringParameter() throws Exception {
        // given
        final Constructor<?> constructor = TestClass1.class.getDeclaredConstructor(String.class);
        final Parameter parameter = constructor.getParameters()[0];
        final ParameterInjectionCandidate injectionCandidate = new ParameterInjectionCandidate(parameter);

        // then
        assertThat(injectionCandidate.getTargetType(), equalTo(TypeLiteral.get(String.class)));
        assertThat(
                injectionCandidate.getElement().getAnnotationsByType(Config.class)[0].value(),
                equalTo("testConfig1"));
    }

    static class TestClass1 {

        public TestClass1(@Config("testConfig1") final String param1) {
        }

    }

}