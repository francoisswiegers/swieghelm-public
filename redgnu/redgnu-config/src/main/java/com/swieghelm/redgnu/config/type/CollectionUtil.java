package com.swieghelm.redgnu.config.type;

import com.google.inject.TypeLiteral;
import com.swieghelm.redgnu.config.convert.exception.TypeConversionException;

import java.util.*;
import java.util.function.Supplier;

public final class CollectionUtil {

    private static final Map<Class<?>, Supplier<Collection<Object>>> TYPE_FACTORIES = new HashMap<>();

    static {
        TYPE_FACTORIES.put(Set.class, HashSet::new);
        TYPE_FACTORIES.put(HashSet.class, HashSet::new);
        TYPE_FACTORIES.put(SortedSet.class, TreeSet::new);
        TYPE_FACTORIES.put(NavigableSet.class, TreeSet::new);
        TYPE_FACTORIES.put(TreeSet.class, TreeSet::new);
        TYPE_FACTORIES.put(LinkedHashSet.class, LinkedHashSet::new);
        TYPE_FACTORIES.put(List.class, ArrayList::new);
        TYPE_FACTORIES.put(ArrayList.class, ArrayList::new);
        TYPE_FACTORIES.put(Collection.class, ArrayList::new);
        TYPE_FACTORIES.put(LinkedList.class, LinkedList::new);
        TYPE_FACTORIES.put(Queue.class, LinkedList::new);
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
        final Supplier<Collection<Object>> supplier = TYPE_FACTORIES.get(typeLiteral.getRawType());
        if (supplier == null) {
            throw new TypeConversionException("Unsupported collection type: " + typeLiteral);
        }
        return supplier.get();
    }

}
