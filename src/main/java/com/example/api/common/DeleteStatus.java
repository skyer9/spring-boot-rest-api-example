package com.example.api.common;

import lombok.Getter;

@Getter
public enum DeleteStatus {
    DELETE(1),
    UNDELETE(0);

    private final Integer value;

    DeleteStatus(Integer value) {
        this.value = value;
    }
}
