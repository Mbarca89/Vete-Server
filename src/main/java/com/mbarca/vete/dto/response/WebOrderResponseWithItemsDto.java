package com.mbarca.vete.dto.response;

import com.mbarca.vete.domain.WebOrderItem;
import lombok.Data;

import java.util.List;

@Data
public class WebOrderResponseWithItemsDto extends WebOrderResponseDto{
    private List<WebOrderItem> items;
}
