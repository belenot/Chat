package com.belenot.web.chat.chat.domain.support;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public interface ModelWrapper<D> {
    public Map<String, Object> wrapUp(D obj);
    public default List<Map<String, Object>> wrapUp(List<D> objList) {
        return objList.stream().map(obj->wrapUp(obj)).collect(Collectors.toList());
    }
}