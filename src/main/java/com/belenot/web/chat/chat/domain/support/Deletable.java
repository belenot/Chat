package com.belenot.web.chat.chat.domain.support;

import java.time.LocalDateTime;

import com.belenot.web.chat.chat.domain.Client;

public interface Deletable {
    LocalDateTime getDeletedTime();
    Client getDeleter();
    boolean isDeleted();
}