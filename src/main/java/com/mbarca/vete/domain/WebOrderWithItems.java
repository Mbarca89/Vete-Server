package com.mbarca.vete.domain;

import lombok.Data;

import java.util.List;

@Data
public class WebOrderWithItems extends WebOrder{
    private List<WebOrderItem> items;
}
