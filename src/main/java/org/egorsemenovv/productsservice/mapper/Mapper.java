package org.egorsemenovv.productsservice.mapper;

public interface Mapper<T, E>{

    E map(T t);
}
