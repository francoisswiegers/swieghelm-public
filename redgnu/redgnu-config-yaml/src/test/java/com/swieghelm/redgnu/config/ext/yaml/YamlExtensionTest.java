package com.swieghelm.redgnu.config.ext.yaml;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.swieghelm.redgnu.config.convert.TypeConversions;
import com.swieghelm.redgnu.config.convert.common.JavaStandardCollectionConversions;
import com.swieghelm.redgnu.config.convert.common.JavaStandardScalarConversions;
import com.swieghelm.redgnu.config.discovery.reflections.ReflectionsInjectionDiscovery;
import com.swieghelm.redgnu.config.extension.vanilla.Config;
import com.swieghelm.redgnu.config.extension.vanilla.DefaultDescriptionDiscovery;
import com.swieghelm.redgnu.config.inject.ConfigModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static java.util.Arrays.asList;
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
                                           new YamlSource(yamlInput))
                                   .setDiscovery(
                                           new ReflectionsInjectionDiscovery(getClass().getPackage(),
                                                   new DefaultDescriptionDiscovery()))
                                   .setTypeConversion(
                                           new TypeConversions(
                                                   new JavaStandardScalarConversions(),
                                                   new JavaStandardCollectionConversions()))
                                   .build();
        injector = Guice.createInjector(configModule);
    }

    @Test
    public void mustInjectSimpleValues() throws Exception {
        final ConfigConsumer configConsumer = injector.getInstance(ConfigConsumer.class);
        assertThat(configConsumer.textValue, equalTo("This is a text sequence"));
        assertThat(configConsumer.notSpecifiedTextValue, equalTo(Optional.empty()));
        assertThat(configConsumer.boolValue, equalTo(true));
        assertThat(configConsumer.bigDecimalValue, equalTo(new BigDecimal("3.14")));
        assertThat(configConsumer.doubleValue, equalTo(3.14D));
        assertThat(configConsumer.textListText, equalTo("[1, 2, 3]"));
        assertThat(configConsumer.textList, equalTo(asList("1", "2", "3")));
        assertThat(configConsumer.bigDecimalList, equalTo(asList(
                new BigDecimal("1"), new BigDecimal("2"), new BigDecimal("3"))));
        assertThat(configConsumer.sortedSet, equalTo(new TreeSet<>(Arrays.asList(1, 2, 3))));
        assertThat(configConsumer.integerMap, equalTo(new HashMap<Integer, Integer>() {
            {
                put(1, 1);
                put(2, 4);
                put(3, 9);
            }
        }));
        assertThat(configConsumer.sortedMap, equalTo(new TreeMap<Integer, String>() {
            {
                put(1, "1");
                put(2, "4");
                put(3, "9");
            }
        }));
        assertThat(configConsumer.stringMap, equalTo(new TreeMap<String, String>() {
            {
                put("1", "1");
                put("2", "4");
                put("3", "9");
            }
        }));
    }

    public static class ConfigConsumer {

        final String textValue;
        final Optional<String> notSpecifiedTextValue;
        final boolean boolValue;
        final BigDecimal bigDecimalValue;
        final double doubleValue;
        final String textListText;
        final List<String> textList;
        final List<BigDecimal> bigDecimalList;
        final SortedSet<Integer> sortedSet;
        final Map<Integer, Integer> integerMap;
        final SortedMap<Integer, String> sortedMap;
        final NavigableMap<String, String> stringMap;

        @Inject
        public ConfigConsumer(final @Config("text") String textValue,
                              final @Config("not-specified") Optional<String> notSpecifiedTextValue,
                              final @Config("boolean") boolean boolValue,
                              final @Config("float") BigDecimal bigDecimalValue,
                              final @Config("float") double doubleValue,
                              final @Config("list") String textListText,
                              final @Config("list") List<String> textList,
                              final @Config("list") List<BigDecimal> bigDecimalList,
                              final @Config("list") SortedSet<Integer> sortedSet,
                              final @Config("map") Map<Integer, Integer> integerMap,
                              final @Config("map") SortedMap<Integer, String> sortedMap,
                              final @Config("map") NavigableMap<String, String> stringMap) {
            this.textValue = textValue;
            this.notSpecifiedTextValue = notSpecifiedTextValue;
            this.boolValue = boolValue;
            this.bigDecimalValue = bigDecimalValue;
            this.doubleValue = doubleValue;
            this.textListText = textListText;
            this.textList = textList;
            this.bigDecimalList = bigDecimalList;
            this.sortedSet = sortedSet;
            this.integerMap = integerMap;
            this.sortedMap = sortedMap;
            this.stringMap = stringMap;
        }

    }

}