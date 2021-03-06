/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jackhuang.hmcl.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 * @author huangyuhui
 */
public final class Lang {

    private Lang() {
    }

    public static final Consumer EMPTY_CONSUMER = a -> {
    };

    public static <K, V> Map<K, V> mapOf(Pair<K, V>... pairs) {
        HashMap<K, V> map = new HashMap<>();
        for (Pair<K, V> pair : pairs)
            map.put(pair.getKey(), pair.getValue());
        return map;
    }

    public static <K, V> V getOrPut(Map<K, V> map, K key, Supplier<V> defaultValue) {
        V value = map.get(key);
        if (value == null) {
            V answer = defaultValue.get();
            map.put(key, answer);
            return answer;
        } else
            return value;
    }

    public static <E extends Throwable> void throwable(Throwable exception) throws E {
        throw (E) exception;
    }

    /**
     * This method will call a method without checked exceptions
     * by treating the compiler.
     *
     * If this method throws a checked exception,
     * it will still abort the application because of the exception.
     *
     * @param <T> type of argument.
     * @param <R> type of result.
     * @param supplier your method.
     * @return the result of the method to invoke.
     */
    public static <T, R, E extends Exception> R invoke(ExceptionalFunction<T, R, E> function, T t) {
        try {
            return function.apply(t);
        } catch (Exception e) {
            throwable(e);
            return null; // won't get to here.
        }
    }

    public static <T, R, E extends Exception> Function<T, R> hideException(ExceptionalFunction<T, R, E> function) {
        return r -> invoke(function, r);
    }

    public static <T, R, E extends Exception> Function<T, R> liftException(ExceptionalFunction<T, R, E> function) throws E {
        return hideException(function);
    }

    /**
     * This method will call a method without checked exceptions
     * by treating the compiler.
     *
     * If this method throws a checked exception,
     * it will still abort the application because of the exception.
     *
     * @param <T> type of result.
     * @param supplier your method.
     * @return the result of the method to invoke.
     */
    public static <T, E extends Exception> T invoke(ExceptionalSupplier<T, E> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            throwable(e);
            return null; // won't get to here.
        }
    }

    public static <T, E extends Exception> Supplier<T> hideException(ExceptionalSupplier<T, E> supplier) {
        return () -> invoke(supplier);
    }

    public static <T, E extends Exception> Supplier<T> liftException(ExceptionalSupplier<T, E> supplier) throws E {
        return hideException(supplier);
    }

    public static <E extends Exception> boolean test(ExceptionalSupplier<Boolean, E> r) {
        try {
            return r.get();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * This method will call a method without checked exceptions
     * by treating the compiler.
     *
     * If this method throws a checked exception,
     * it will still abort the application because of the exception.
     *
     * @param r your method.
     */
    public static <E extends Exception> void invoke(ExceptionalRunnable<E> r) {
        try {
            r.run();
        } catch (Exception e) {
            throwable(e);
        }
    }

    public static <E extends Exception> Runnable hideException(ExceptionalRunnable<E> r) {
        return () -> invoke(r);
    }

    public static <E extends Exception> Runnable liftException(ExceptionalRunnable<E> r) throws E {
        return hideException(r);
    }

    public static <E extends Exception> boolean test(ExceptionalRunnable<E> r) {
        try {
            r.run();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static <T> T ignoringException(ExceptionalSupplier<T, ?> supplier) {
        return ignoringException(supplier, null);
    }

    public static <T> T ignoringException(ExceptionalSupplier<T, ?> supplier, T defaultValue) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static void ignoringException(ExceptionalRunnable<?> runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
        }
    }

    public static <V> Optional<V> convert(Object o, Class<V> clazz) {
        if (o == null || !ReflectionHelper.isInstance(clazz, o))
            return Optional.<V>empty();
        else
            return Optional.of((V) o);
    }

    public static <V> V convert(Object o, Class<V> clazz, V defaultValue) {
        if (o == null || !ReflectionHelper.isInstance(clazz, o))
            return defaultValue;
        else
            return (V) o;
    }

    public static <V> Optional<V> get(Map<?, ?> map, Object key, Class<V> clazz) {
        return convert(map.get(key), clazz);
    }

    public static <V> V get(Map<?, ?> map, Object key, Class<V> clazz, V defaultValue) {
        return convert(map.get(key), clazz, defaultValue);
    }

    public static <T> List<T> merge(Collection<T>... collections) {
        LinkedList<T> result = new LinkedList<>();
        for (Collection<T> collection : collections)
            if (collection != null)
                result.addAll(collection);
        return result;
    }

    public static <T> T nonNull(T... t) {
        for (T a : t)
            if (a != null)
                return a;
        return null;
    }

    public static Thread thread(Runnable runnable) {
        return thread(runnable, null);
    }

    public static Thread thread(Runnable runnable, String name) {
        return thread(runnable, name, false);
    }

    public static Thread thread(Runnable runnable, String name, boolean isDaemon) {
        Thread thread = new Thread(runnable);
        if (isDaemon)
            thread.setDaemon(true);
        if (name != null)
            thread.setName(name);
        thread.start();
        return thread;
    }

    public static <T> T get(Optional<T> optional) {
        return optional.isPresent() ? optional.get() : null;
    }

    public static <T> Iterator<T> asIterator(Enumeration<T> enumeration) {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return enumeration.hasMoreElements();
            }

            @Override
            public T next() {
                return enumeration.nextElement();
            }
        };
    }

    public static <T> Iterable<T> asIterable(Enumeration<T> enumeration) {
        return () -> asIterator(enumeration);
    }
}
