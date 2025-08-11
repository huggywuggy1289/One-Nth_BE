package com.onenth.OneNth.domain.product.entity;

import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import com.onenth.OneNth.domain.product.entity.enums.Status;

import java.util.List;

public interface Item {
    void setStatus(Status status);
    Status getStatus();
    String getProductName();
    Long getId();
    ItemType getItemType();
    List<ItemImage> getItemImages();
}
