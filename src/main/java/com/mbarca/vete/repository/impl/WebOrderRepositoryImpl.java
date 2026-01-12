package com.mbarca.vete.repository.impl;

import com.mbarca.vete.domain.WebOrder;
import com.mbarca.vete.repository.WebOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
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

    @Override
    public List<WebOrder> findByDate(Date dateStart, Date dateEnd){
        String GET_ORDERS_BY_DATE = "SELECT * FROM web_orders WHERE created_at >= ? AND created_at <= ?";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateEnd);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        dateEnd = calendar.getTime();
        Object[] params = {dateStart, dateEnd};
        return jdbcTemplate.query(GET_ORDERS_BY_DATE, params, mapper);
    }

    @Override
    public int shipOrder(Long orderId){
        String SHIP_ORDER = "UPDATE web_orders SET shipped = true WHERE id=?";
        Object[] params = {orderId};
        return jdbcTemplate.update(SHIP_ORDER,params);
    }

    private static final String FIND_PENDING_OLDER = """
        SELECT id, customer_name, customer_email, customer_phone, total_amount, status,
               preference_id, payment_id, created_at
        FROM web_orders
        WHERE status = 'PENDING'
          AND created_at < DATEADD('MINUTE', -?, CURRENT_TIMESTAMP)
        ORDER BY created_at ASC
    """;

    private static final String UPDATE_STATUS_AND_PAYMENT_IF_PENDING = """
        UPDATE web_orders
        SET status = ?,
            payment_id = ?
        WHERE id = ?
          AND status = 'PENDING'
    """;

    private static final String UPDATE_STATUS_IF_PENDING = """
        UPDATE web_orders
        SET status = ?
        WHERE id = ?
          AND status = 'PENDING'
    """;

    private static final String UPDATE_PAYMENT_IF_NULL = """
        UPDATE web_orders
        SET payment_id = ?
        WHERE id = ?
          AND (payment_id IS NULL OR payment_id = '')
    """;

    private final RowMapper<WebOrder> mapper = (rs, rowNum) -> {
        WebOrder o = new WebOrder();
        o.setId(rs.getLong("id"));
        o.setCustomerName(rs.getString("customer_name"));
        o.setCustomerEmail(rs.getString("customer_email"));
        o.setCustomerPhone(rs.getString("customer_phone"));
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        o.setStatus(rs.getString("status"));
        o.setPreferenceId(rs.getString("preference_id"));
        o.setPaymentId(rs.getString("payment_id"));
        o.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        o.setShipped(rs.getBoolean("shipped"));
        return o;
    };

    @Override
    public List<WebOrder> findPendingOlderThanMinutes(int minutes) {
        return jdbcTemplate.query(FIND_PENDING_OLDER, mapper, minutes);
    }

    @Override
    public int updateStatusAndPaymentIfPending(Long orderId, String status, String paymentId) {
        return jdbcTemplate.update(UPDATE_STATUS_AND_PAYMENT_IF_PENDING, status, paymentId, orderId);
    }

    @Override
    public int updateStatusIfPending(Long orderId, String status) {
        return jdbcTemplate.update(UPDATE_STATUS_IF_PENDING, status, orderId);
    }

    @Override
    public int updatePaymentIdIfNull(Long orderId, String paymentId) {
        return jdbcTemplate.update(UPDATE_PAYMENT_IF_NULL, paymentId, orderId);
    }

}
