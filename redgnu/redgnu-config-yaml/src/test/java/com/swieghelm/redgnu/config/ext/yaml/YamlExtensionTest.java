package com.swieghelm.redgnu.config.ext.yaml;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.swieghelm.redgnu.config.convert.common.SimpleJavaStandardTypeConversions;
import com.swieghelm.redgnu.config.discovery.reflections.ReflectionsInjectionDiscovery;
import com.swieghelm.redgnu.config.extension.vanilla.Config;
import com.swieghelm.redgnu.config.extension.vanilla.DefaultDescriptionDiscovery;
import com.swieghelm.redgnu.config.inject.ConfigModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;

import java.io.InputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class YamlExtensionTest {

    private ConfigModule configModule;
    private Injector injector;

    @BeforeEach
    public void beforeEach() throws Exception {
        final InputStream yamlInput = getClass().getClassLoader().getResourceAsStream("simple.yaml");
        configModule = ConfigModule.newBuilder()
                                          .setConfigSource(
                                                  new YamlSource(
                                                          new Load(LoadSettings.builder().build()), yamlInput))
                                          .setDiscovery(
                                                  new ReflectionsInjectionDiscovery(getClass().getPackage(),
                                                          new DefaultDescriptionDiscovery()))
                                          .setTypeConversion(new SimpleJavaStandardTypeConversions())
                                          .build();
        injector = Guice.createInjector(configModule);
    }

    @Test
    public void mustInjectSimpleValues() throws Exception {
        final ConfigConsumer configConsumer = injector.getInstance(ConfigConsumer.class);
        assertThat(configConsumer.textValue, equalTo("This is a text sequence"));
    }


    public static class ConfigConsumer {

        final String textValue;

        @Inject
        public ConfigConsumer(final @Config("text") String textValue) {
            this.textValue = textValue;
        }

    }

}
