package com.mbarca.vete.repository.impl;

import com.mbarca.vete.domain.WebOrder;
import com.mbarca.vete.repository.WebOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
@RequiredArgsConstructor
public class WebOrderRepositoryImpl implements WebOrderRepository {
    private final JdbcTemplate jdbcTemplate;

    public Long create(WebOrder order) {
        String sql = """
                    INSERT INTO web_orders
                    (customer_name, customer_email, customer_phone, total_amount, status)
                    VALUES (?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, order.getCustomerName());
            ps.setString(2, order.getCustomerEmail());
            ps.setString(3, order.getCustomerPhone());
            ps.setBigDecimal(4, order.getTotalAmount());
            ps.setString(5, order.getStatus());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void updatePreference(Long orderId, String preferenceId) {
        jdbcTemplate.update(
                "UPDATE web_orders SET preference_id = ? WHERE id = ?",
                preferenceId, orderId
        );
    }

    public void updatePayment(Long orderId, String paymentId, String status) {
        jdbcTemplate.update(
                "UPDATE web_orders SET payment_id = ?, status = ? WHERE id = ?",
                paymentId, status, orderId
        );
    }

    public WebOrder findById(Long id) {
        return jdbcTemplate.queryForObject("""
        SELECT * FROM web_orders WHERE id = ?
    """, new BeanPropertyRowMapper<>(WebOrder.class), id);
    }

}
