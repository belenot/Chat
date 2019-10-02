package com.belenot.web.chat.chat.model;

public interface DomainModel<T> {
    T createDomain();
    default void updateDomain(T domain){ /* do nothing */ }
    

}