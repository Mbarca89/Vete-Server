package com.mbarca.vete.repository.impl;

import com.mbarca.vete.domain.Order;
import com.mbarca.vete.domain.OrderProduct;
import com.mbarca.vete.domain.Sale;
import com.mbarca.vete.domain.SaleProduct;
import com.mbarca.vete.repository.OrderRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;
import java.util.Date;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
    private final String CREATE_ORDER = "INSERT INTO Orders (order_date, order_amount) VALUES (?,?)";
    private final String UPDATE_PRODUCT = "UPDATE Products SET stock = stock + ?, cost = ?, price = ? WHERE id = ?";
    private final String CREATE_ORDER_PRODUCTS =
            "INSERT INTO OrdersProducts (" +
                    "order_id, product_id, quantity, " +
                    "product_name, product_description, " +
                    "product_price, product_cost" +
                    ") VALUES (?,?,?,?,?,?,?)";

    private final String GET_ORDERS_BY_DATE = "SELECT * FROM Orders WHERE order_date >= ? AND order_date <= ?";
    private final String GET_ORDER_AND_PRODUCTS =
            "SELECT o.id AS order_id, o.order_date, o.order_amount, " +
                    "op.product_id, op.quantity, " +
                    "op.product_name, op.product_description, op.product_price, op.product_cost, " +
                    "op.product_bar_code " +
                    "FROM Orders o " +
                    "INNER JOIN OrdersProducts op ON o.id = op.order_id " +
                    "WHERE o.id = ?";

    JdbcTemplate jdbcTemplate;

    public OrderRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer createOrder(Order order) {
        return jdbcTemplate.execute((ConnectionCallback<Integer>) connection -> {
            connection.setAutoCommit(false);
            try {
                PreparedStatement saleStatement = connection.prepareStatement(CREATE_ORDER, Statement.RETURN_GENERATED_KEYS);
                saleStatement.setTimestamp(1, new Timestamp(order.getDate().getTime()));
                saleStatement.setBigDecimal(2, order.getAmount());
                saleStatement.executeUpdate();

                ResultSet generatedKeys = saleStatement.getGeneratedKeys();
                long orderId = -1;
                if (generatedKeys.next()) {
                    orderId = generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Failed to get sale ID, no keys were generated.");
                }

                for (OrderProduct orderProduct : order.getOrderProducts()) {
                    PreparedStatement stockStatement = connection.prepareStatement(UPDATE_PRODUCT);
                    stockStatement.setInt(1, orderProduct.getQuantity());
                    stockStatement.setString(2, orderProduct.getProductCost());
                    stockStatement.setString(3, orderProduct.getProductPrice());
                    stockStatement.setLong(4, orderProduct.getProductId());
                    stockStatement.executeUpdate();

                    PreparedStatement orderProductStatement = connection.prepareStatement(CREATE_ORDER_PRODUCTS);
                    orderProductStatement.setLong(1, orderId);
                    orderProductStatement.setLong(2, orderProduct.getProductId());
                    orderProductStatement.setInt(3, orderProduct.getQuantity());
                    orderProductStatement.setString(4, orderProduct.getProductName());
                    orderProductStatement.setString(5, orderProduct.getProductDescription());
                    orderProductStatement.setString(6, orderProduct.getProductPrice());
                    orderProductStatement.setString(7, orderProduct.getProductCost());

                    orderProductStatement.executeUpdate();
                }

                connection.commit();

                return 1;
            } catch (SQLException e) {
                connection.rollback();
                throw new SQLException(e.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }
        });
    }

    @Override
    public Order getOrderWithProductsById(long orderId) {
        return jdbcTemplate.query(GET_ORDER_AND_PRODUCTS, new OrderWithProductsResultSetExtractor(), orderId);
    }

    @Override
    public List<Order> getOrdersByDate(Date dateStart, Date dateEnd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateEnd);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        dateEnd = calendar.getTime();
        Object[] params = {dateStart, dateEnd};
        return jdbcTemplate.query(GET_ORDERS_BY_DATE, params, new OrderRowMapper());
    }

    static class OrderWithProductsResultSetExtractor implements ResultSetExtractor<Order> {
        @Override
        public Order extractData(ResultSet rs) throws SQLException, DataAccessException {
            Order order = null;
            Map<Long, Order> orderMap = new HashMap<>();

            while (rs.next()) {
                long orderId = rs.getLong("order_id");

                if (order == null) {
                    order = new Order();
                    order.setId(orderId);
                    order.setDate(rs.getTimestamp("order_date"));
                    order.setAmount(rs.getBigDecimal("order_amount"));
                    order.setOrderProducts(new ArrayList<>());
                }

                OrderProduct orderProduct = new OrderProduct();
                orderProduct.setOrderId(orderId);
                orderProduct.setProductId(rs.getLong("product_id"));
                orderProduct.setQuantity(rs.getInt("quantity"));
                orderProduct.setProductName(rs.getString("product_name"));
                orderProduct.setProductDescription(rs.getString("product_description"));
                orderProduct.setProductPrice(rs.getString("product_price"));
                orderProduct.setProductCost(rs.getString("product_cost"));

                order.getOrderProducts().add(orderProduct);
            }

            return order;
        }
    }

    static class OrderRowMapper implements RowMapper<Order> {
        @Override
        public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
            Order order = new Order();
            order.setId(rs.getLong("id"));
            order.setDate(rs.getTimestamp("order_date"));
            order.setAmount(rs.getBigDecimal("order_amount"));
            return order;
        }
    }
}
