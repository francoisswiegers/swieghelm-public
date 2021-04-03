package com.swieghelm.redgnu.config.type;

import com.google.inject.TypeLiteral;
import com.swieghelm.redgnu.config.convert.exception.TypeConversionException;

import java.util.*;
import java.util.function.Supplier;

public final class CollectionUtil {

    private static final Map<Class<?>, Supplier<Collection<Object>>> COL_TYPE_FACTORIES = new HashMap<>();
    private static final Map<Class<?>, Supplier<Map<Object, Object>>> MAP_TYPE_FACTORIES = new HashMap<>();

    static {
        COL_TYPE_FACTORIES.put(Set.class, HashSet::new);
        COL_TYPE_FACTORIES.put(HashSet.class, HashSet::new);
        COL_TYPE_FACTORIES.put(SortedSet.class, TreeSet::new);
        COL_TYPE_FACTORIES.put(NavigableSet.class, TreeSet::new);
        COL_TYPE_FACTORIES.put(TreeSet.class, TreeSet::new);
        COL_TYPE_FACTORIES.put(LinkedHashSet.class, LinkedHashSet::new);
        COL_TYPE_FACTORIES.put(List.class, ArrayList::new);
        COL_TYPE_FACTORIES.put(ArrayList.class, ArrayList::new);
        COL_TYPE_FACTORIES.put(Collection.class, ArrayList::new);
        COL_TYPE_FACTORIES.put(LinkedList.class, LinkedList::new);
        COL_TYPE_FACTORIES.put(Queue.class, LinkedList::new);

        MAP_TYPE_FACTORIES.put(Map.class, HashMap::new);
        MAP_TYPE_FACTORIES.put(HashMap.class, HashMap::new);
        MAP_TYPE_FACTORIES.put(NavigableMap.class, TreeMap::new);
        MAP_TYPE_FACTORIES.put(SortedMap.class, TreeMap::new);
        MAP_TYPE_FACTORIES.put(TreeMap.class, TreeMap::new);
        MAP_TYPE_FACTORIES.put(LinkedHashMap.class, LinkedHashMap::new);
    }

    private CollectionUtil() {
    }

    public static boolean isCollection(final TypeLiteral<?> typeLiteral) {
        return Collection.class.isAssignableFrom(typeLiteral.getRawType());
    }

    public static boolean isSet(final TypeLiteral<?> typeLiteral) {
        return Set.class.isAssignableFrom(typeLiteral.getRawType());
    }

    public static boolean isMap(final TypeLiteral<?> typeLiteral) {
        return Map.class.isAssignableFrom(typeLiteral.getRawType());
    }

    public static Collection<Object> createInstance(final TypeLiteral<?> typeLiteral) {
        final Supplier<Collection<Object>> supplier = COL_TYPE_FACTORIES.get(typeLiteral.getRawType());
        if (supplier == null) {
            throw new TypeConversionException("Unsupported collection type: " + typeLiteral);
        }
        return supplier.get();
    }

    public static Map<Object, Object> createMapInstance(final TypeLiteral<?> typeLiteral) {
        final Supplier<Map<Object, Object>> supplier = MAP_TYPE_FACTORIES.get(typeLiteral.getRawType());
        if (supplier == null) {
            throw new TypeConversionException("Unsupported map type: " + typeLiteral);
        }
        return supplier.get();
    }

}
