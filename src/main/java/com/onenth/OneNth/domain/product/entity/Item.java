package com.onenth.OneNth.domain.product.entity;

import com.onenth.OneNth.domain.product.entity.enums.Status;

public interface Item {
    void setStatus(Status status);
    Status getStatus();
}
