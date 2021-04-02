package tests.com.swieghelm.redgnu.config.simple;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.swieghelm.redgnu.config.convert.common.JavaStandardScalarConversions;
import com.swieghelm.redgnu.config.discovery.reflections.ReflectionsInjectionDiscovery;
import com.swieghelm.redgnu.config.extension.vanilla.Config;
import com.swieghelm.redgnu.config.extension.vanilla.DefaultDescriptionDiscovery;
import com.swieghelm.redgnu.config.inject.ConfigModule;
import com.swieghelm.redgnu.config.source.map.MapConfigSource;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class SimpleInjectionsTest {

    @Test
    public void mustInjectScalarValues() throws Exception {
        final Map<String, String> configs = new HashMap<>();
        configs.put("string_config", "testValue");
        configs.put("double_config", "20.00");

        final Injector injector = Guice.createInjector(
                ConfigModule.newBuilder()
                            .setConfigSource(new MapConfigSource(configs))
                            .setDiscovery(new ReflectionsInjectionDiscovery(
                                    getClass().getPackage(),
                                    new DefaultDescriptionDiscovery()))
                            .setTypeConversion(new JavaStandardScalarConversions())
                            .build());

        final ConfiguredType configuredType = injector.getInstance(ConfiguredType.class);

        assertThat(configuredType.strValue1, equalTo(Optional.of("testValue")));
        assertThat(configuredType.strValue2, equalTo("testValue"));
        assertThat(configuredType.strValue3, equalTo(Optional.empty()));
        assertThat(configuredType.strValue4, equalTo("default_value"));
        assertThat(configuredType.strValue5, equalTo(Optional.of("default_value")));
        assertThat(configuredType.doubleValue1, equalTo(Optional.of(Double.valueOf("20.00"))));
        assertThat(configuredType.doubleValue2, equalTo(Double.valueOf("20.00")));
        assertThat(configuredType.doubleValue3, equalTo(20.0D));
        assertThat(configuredType.doubleValue4, equalTo(Optional.empty()));
        assertThat(configuredType.doubleValue5, equalTo(70.0D));
    }

    static class ConfiguredType {

        final Optional<String> strValue1;
        final String strValue2;
        final Optional<String> strValue3;
        final String strValue4;
        final Optional<String> strValue5;
        final Optional<Double> doubleValue1;
        final Double doubleValue2;
        final double doubleValue3;
        final Optional<Double> doubleValue4;
        final double doubleValue5;

        @Inject
        public ConfiguredType(final @Config("string_config") Optional<String> strValue1,
                              final @Config(value = "string_config", defaultValue = "default_value") String strValue2,
                              final @Config("not_configured") Optional<String> strValue3,
                              final @Config(value = "not_configured", defaultValue = "default_value") String strValue4,
                              final @Config(value = "not_configured", defaultValue = "default_value") Optional<String> strValue5,
                              final @Config("double_config") Optional<Double> doubleValue1,
                              final @Config(value = "double_config", defaultValue = "50.00") Double doubleValue2,
                              final @Config(value = "double_config", defaultValue = "60.00") double doubleValue3,
                              final @Config("not_configured") Optional<Double> doubleValue4,
                              final @Config(value = "not_configured", defaultValue = "70.00") double doubleValue5) {
            this.strValue1 = strValue1;
            this.strValue2 = strValue2;
            this.strValue3 = strValue3;
            this.strValue4 = strValue4;
            this.strValue5 = strValue5;
            this.doubleValue1 = doubleValue1;
            this.doubleValue2 = doubleValue2;
            this.doubleValue3 = doubleValue3;
            this.doubleValue4 = doubleValue4;
            this.doubleValue5 = doubleValue5;
        }

    }

}