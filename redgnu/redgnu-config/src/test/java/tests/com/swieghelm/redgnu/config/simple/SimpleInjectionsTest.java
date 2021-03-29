package tests.com.swieghelm.redgnu.config.simple;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.swieghelm.redgnu.config.convert.common.SimpleJavaStandardTypeConversions;
import com.swieghelm.redgnu.config.extension.vanilla.Config;
import com.swieghelm.redgnu.config.extension.vanilla.DefaultDescriptionDiscovery;
import com.swieghelm.redgnu.config.inject.ConfigModule;
import com.swieghelm.redgnu.config.source.impl.MapConfigSource;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class SimpleInjectionsTest {

    @Test
    public void mustInjectSimpleValues() throws Exception {
        final Map<String, String> configs = new HashMap<>();
        configs.put("string_config", "testValue");
        configs.put("double_config", "20.00");

        final Injector injector = Guice.createInjector(
                ConfigModule.newBuilder()
                            .setConfigSource(new MapConfigSource(configs))
                            .setDescriptionDiscovery(new DefaultDescriptionDiscovery())
                            .setReflections(new Reflections(getClass().getPackage().getName()))
                            .setTypeConversion(new SimpleJavaStandardTypeConversions())
                            .build());

        final ConfiguredType configuredType = injector.getInstance(ConfiguredType.class);

        assertThat(configuredType.strValue1, equalTo(Optional.of("testValue")));
        assertThat(configuredType.strValue2, equalTo("testValue"));
        assertThat(configuredType.doubleValue1, equalTo(Optional.of(Double.valueOf("20.00"))));
        assertThat(configuredType.doubleValue2, equalTo(Double.valueOf("20.00")));
        assertThat(configuredType.doubleValue3, equalTo(20.00D));
    }

    static class ConfiguredType {

        final Optional<String> strValue1;
        final String strValue2;
        final Optional<Double> doubleValue1;
        final Double doubleValue2;
        final double doubleValue3;

        @Inject
        public ConfiguredType(final @Config("string_config") Optional<String> strValue1,
                              final @Config(value = "string_config", defaultValue = "default2") String strValue2,
                              final @Config("double_config") Optional<Double> doubleValue1,
                              final @Config(value = "double_config", defaultValue = "50.00") Double doubleValue2,
                              final @Config(value = "double_config", defaultValue = "60.00") double doubleValue3) {
            this.strValue1 = strValue1;
            this.strValue2 = strValue2;
            this.doubleValue1 = doubleValue1;
            this.doubleValue2 = doubleValue2;
            this.doubleValue3 = doubleValue3;
        }

    }

}