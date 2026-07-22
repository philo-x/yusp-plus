package cn.com.yusys.yusp.common.utils;

/**
 * Key-Value值Consumer
 *
 * @author xufy1
 * @date 2020-11-19 15:30
 */
@FunctionalInterface
public interface KeyValueConsumer<T, K, V> {

    /**
     * accept
     * @param t
     * @param k
     * @param v
     */
    void accept(T t, K k, V v);
}

