package com.belenot.web.chat.chat.domain.support;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public interface ModelWrapper<D> {
    public Map<String, Object> wrapUp(D obj);
}