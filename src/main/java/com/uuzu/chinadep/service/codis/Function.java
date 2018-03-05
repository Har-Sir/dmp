package com.uuzu.chinadep.service.codis;

/**
 * Created by lixing on 2017/3/23.
 */
public interface Function<T,E> {
    T callback(E e);
}
