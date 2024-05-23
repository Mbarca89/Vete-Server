package com.mbarca.vete.repository.impl;

import com.mbarca.vete.domain.Payment;
import com.mbarca.vete.domain.Vaccine;
import com.mbarca.vete.repository.PaymentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Repository
public class PaymentRepositoryImpl implements PaymentRepository {
    private final String CREATE_PAYMENT = "INSERT INTO Payments (date, bill_number, amount, provider, payed, payment_method, payment_date) VALUES (?,?,?,?,?,?,?)";
    private final String MAKE_PAYMENT = "UPDATE Payments SET payed = ?, payment_method = ?, payment_date = ? WHERE id = ?";
    private final String GET_PAYMENTS = "SELECT * FROM Payments WHERE date BETWEEN ? AND ?";
    private final String GET_PAYMENT_BY_ID = "SELECT * FROM Payments WHERE id = ?";
    JdbcTemplate jdbcTemplate;

    public PaymentRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer createPayment(Payment payment) {
        return jdbcTemplate.update(CREATE_PAYMENT,
                new Date(),
                payment.getBillNumber(),
                payment.getAmount(),
                payment.getProvider(),
                payment.getPayed(),
                payment.getPaymentMethod(),
                payment.getPayed() ? new Date() : null
                );
    }

    @Override
    public Integer makePayment(Payment payment) {
        return jdbcTemplate.update(MAKE_PAYMENT,
                payment.getPayed(),
                payment.getPaymentMethod(),
                new Date(),
                payment.getId()
                );
    }

    @Override
    public List<Payment> getPayments(Date dateStart, Date dateEnd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateEnd);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        dateEnd = calendar.getTime();
        Object[] params = {dateStart, dateEnd};
        return jdbcTemplate.query(GET_PAYMENTS, params, new PaymentRowMapper());
    }

    @Override
    public Payment getPaymentById(Long paymentId) {
        Object[] params = {paymentId};
        int[] types = {1};
        return jdbcTemplate.queryForObject(GET_PAYMENT_BY_ID, params, types, new PaymentRowMapper());
    }


    static class PaymentRowMapper implements RowMapper<Payment> {
        @Override
        public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
            Payment payment = new Payment();
            payment.setId(rs.getLong("id"));
            payment.setDate(rs.getDate("date"));
            payment.setBillNumber(rs.getString("bill_number"));
            payment.setAmount(rs.getDouble("amount"));
            payment.setProvider(rs.getString("provider"));
            payment.setPayed(rs.getBoolean("payed"));
            payment.setPaymentMethod(rs.getString("payment_method"));
            payment.setPaymentDate(rs.getDate("payment_date"));
            return payment;
        }
    }
}
