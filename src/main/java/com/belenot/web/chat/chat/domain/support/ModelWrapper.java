package com.belenot.web.chat.chat.domain.support;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public interface ModelWrapper<T> {
    public Map<String, Object> wrapUp(T obj);
    public default List<Map<String, Object>> wrapUp(List<T> objList) {
        return objList.stream().map(obj->wrapUp(obj)).collect(Collectors.toList());
    }
}