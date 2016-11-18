package com.github.wings27.common.utils;

import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Project common-utils
 * Created by wenqiushi on 2016-11-18 16:55.
 */
public class CollectionUtils {

    /**
     * 对于传入的item collection，按照keyFunction提取item key。返回 键为item key、值为item本身的Map
     * <p/>
     * 例：传入学生集合，返回学号作为键的map
     * <p/>
     * items -> { keyFunction(item) : item }
     */
    public static <T, K> Map<K, T> uniqueIndex(Collection<T> collection, Function<? super T, K> keyFunction) {
        return mapAndCollectToMap(collection, keyFunction, Function.identity());
    }

    /**
     * 对于传入的item collection，按照keyFunction提取item key。返回 键为item key、值为item本身的Map
     * <p/>
     * 例：传入学生集合，返回学号作为键的map
     * <p/>
     * items -> { keyFunction(item) : item }
     */
    public static <T, K> Map<K, T> uniqueIndex(Collection<T> collection, Function<? super T, ? extends K> keyFunction, BinaryOperator<T> mergeFunction) {
        return mapAndCollectToMap(collection, keyFunction, Function.identity(), mergeFunction);
    }

    /**
     * 对于传入的item collection，按照classifier分组，返回 键为item key、值为grouped items的Map
     * <p/>
     * 例：传入学生集合，返回按照班级分组的map，值为该班级的学生列表。
     * <p/>
     * items -> { classifier(item) : grouped items }
     */
    public static <T, K> Map<K, List<T>> groupByToList(Collection<T> collection, Function<? super T, ? extends K> classifier) {
        if (collection == null) {
            return null;
        }

        return collection.stream().collect(Collectors.groupingBy(classifier, Collectors.toList()));
    }

    /**
     * 对于传入的item collection，按照classifier分组，返回 键为item key、值为grouped items的Map
     * <p/>
     * 例：传入学生集合，返回按照班级分组的map，值为该班级的学生集合（去重）。
     * <p/>
     * items -> { classifier(item) : grouped items }
     */
    public static <T, K> Map<K, Set<T>> groupByToSet(Collection<T> collection, Function<? super T, ? extends K> classifier) {
        if (collection == null) {
            return null;
        }

        return collection.stream().collect(Collectors.groupingBy(classifier, Collectors.toSet()));
    }

    /**
     * 对item collection按照classifier进行分组，存入Multimap。Multimap的值是item经过valueFunction映射后的结果
     * <p/>
     * 例：传入学生集合，返回按照班级分组的Multimap，值为该班级的学生的姓名。
     */
    public static <T, R1, R2> Multimap<R1, R2> multimapGroupBy(
            Collection<T> collection, Function<T, R1> classifier,
            Function<T, R2> valueFunction, Supplier<? extends Multimap<R1, R2>> supplier) {
        return collection.stream().collect(supplier,
                (map, item) -> map.put(classifier.apply(item), valueFunction.apply(item)),
                Multimap::putAll);
    }

    /**
     * 对item collection按照classifier进行分组，存入Multimap。Multimap的值是item经过valueFunction映射后得到的集合展开的结果
     * <p/>
     * 例：传入学生集合，返回按照班级分组的Multimap，值为该班级的学生的选课列表。
     */
    public static <T, R1, R2, M extends Multimap<R1, R2>> M multimapGroupByFlat(
            Collection<T> collection, Function<T, R1> classifier,
            Function<T, ? extends Collection<R2>> valueFunction, Supplier<M> supplier) {
        return collection.stream().collect(supplier,
                (map, item) -> map.putAll(classifier.apply(item), valueFunction.apply(item)),
                Multimap::putAll);
    }

    /**
     * 对collection按照mapFunction映射，并collect成Set
     */
    public static <T, K> Set<K> mapAndCollectToSet(Collection<T> collection
            , Function<? super T, ? extends K> mapFunction) {
        if (collection == null) {
            return null;
        }

        return collection.stream().map(mapFunction).collect(Collectors.toSet());
    }

    /**
     * 对collection按照mapFunction映射，并collect成List
     */
    public static <T, K> List<K> mapAndCollectToList(Collection<T> collection
            , Function<? super T, ? extends K> mapFunction) {
        if (collection == null) {
            return null;
        }

        return collection.stream().map(mapFunction).collect(Collectors.toList());
    }

    /**
     * java非要把dictionary叫map我怎么都觉得会有歧义哎╭(╯^╰)╮
     * 反正就是对collection按照keyFunction和valueFunction映射成java.util.Map.
     */
    public static <T, K, V> Map<K, V> mapAndCollectToMap(Collection<T> collection
            , Function<? super T, ? extends K> keyFunction
            , Function<? super T, ? extends V> valueFunction) {
        return mapAndCollectToMap(collection, keyFunction, valueFunction, (k, v) -> {
            throw new IllegalStateException(String.format("Duplicate key %s", k));
        });
    }

    /**
     * java非要把dictionary叫map我怎么都觉得会有歧义哎╭(╯^╰)╮
     * 反正就是对collection按照keyFunction和valueFunction映射成java.util.Map.
     */
    public static <T, K, V> Map<K, V> mapAndCollectToMap(Collection<T> collection
            , Function<? super T, ? extends K> keyFunction
            , Function<? super T, ? extends V> valueFunction
            , BinaryOperator<V> mergeFunction) {
        if (collection == null) {
            return null;
        }

        return collection.stream().collect(Collectors.toMap(keyFunction, valueFunction, mergeFunction));
    }

}
