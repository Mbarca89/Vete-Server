package com.mbarca.vete.repository.impl;

import com.mbarca.vete.domain.WebOrderItem;
import com.mbarca.vete.repository.WebOrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WebOrderItemRepositoryImpl implements WebOrderItemRepository {
    private final JdbcTemplate jdbcTemplate;

    public void create(WebOrderItem item) {
        jdbcTemplate.update("""
            INSERT INTO web_order_items
            (web_order_id, product_id, product_name, quantity, unit_price)
            VALUES (?, ?, ?, ?, ?)
        """,
                item.getWebOrderId(),
                item.getProductId(),
                item.getProductName(),
                item.getQuantity(),
                item.getUnitPrice()
        );
    }

    public List<WebOrderItem> findByOrderId(Long orderId) {
        return jdbcTemplate.query("""
        SELECT * FROM web_order_items WHERE web_order_id = ?
    """, new BeanPropertyRowMapper<>(WebOrderItem.class), orderId);
    }
}
