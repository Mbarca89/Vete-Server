package com.mbarca.vete.repository.impl;

import com.mbarca.vete.domain.CategoryTotal;
import com.mbarca.vete.domain.MonthlyReport;
import com.mbarca.vete.domain.Sale;
import com.mbarca.vete.domain.SaleProduct;
import com.mbarca.vete.repository.SaleRepository;
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
public class SaleRepositoryImpl implements SaleRepository {
    private final String CREATE_SALE = "INSERT INTO Sales (sale_date, sale_amount, sale_cost, seller) VALUES (?,?,?,?)";
    private final String CREATE_SALE_PRODUCTS = "INSERT INTO SalesProducts (sale_id, product_id, quantity) VALUES (?,?,?)";
    private final String GET_SALE_AND_PRODUCTS = "SELECT s.id AS sale_id, s.sale_date, s.sale_amount, s.sale_cost, s.seller, " +
            "sp.product_id, sp.quantity, " +
            "p.name AS product_name, p.bar_code AS product_barcode, p.description AS product_description, p.price AS product_price, p.cost AS product_cost " +
            "FROM Sales s " +
            "INNER JOIN SalesProducts sp ON s.id = sp.sale_id " +
            "INNER JOIN Products p ON sp.product_id = p.id " +
            "WHERE s.id = ?";

    private final String UPDATE_STOCK = "UPDATE Products SET stock = stock - ? WHERE id = ?";
    private final String GET_SALES_BY_DATE = "SELECT * FROM Sales WHERE sale_date >= ? AND sale_date <= ?";
    private final String GET_CATEGORY_NAMES = "SELECT DISTINCT name FROM Category";
    private final String GET_SALES_BY_CATEGORY = "SELECT SUM(p.price * sp.quantity) AS total_amount " +
            "FROM Sales s " +
            "INNER JOIN SalesProducts sp ON s.id = sp.sale_id " +
            "INNER JOIN Products p ON sp.product_id = p.id " +
            "WHERE p.category_name = ? AND "+
            "sale_date BETWEEN ? AND ?";
    JdbcTemplate jdbcTemplate;

    public SaleRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer createSale(Sale sale) {
        Integer res = jdbcTemplate.execute((ConnectionCallback<Integer>) connection -> {
            connection.setAutoCommit(false);
            try {
                PreparedStatement saleStatement = connection.prepareStatement(CREATE_SALE, Statement.RETURN_GENERATED_KEYS);
                saleStatement.setTimestamp(1, new Timestamp(sale.getDate().getTime()));
                saleStatement.setBigDecimal(2, sale.getAmount());
                saleStatement.setBigDecimal(3, sale.getCost());
                saleStatement.setString(4, sale.getSeller());
                saleStatement.executeUpdate();

                ResultSet generatedKeys = saleStatement.getGeneratedKeys();
                long saleId = -1;
                if (generatedKeys.next()) {
                    saleId = generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Failed to get sale ID, no keys were generated.");
                }

                for (SaleProduct saleProduct : sale.getSaleProducts()) {

                    PreparedStatement stockStatement = connection.prepareStatement(UPDATE_STOCK);
                    stockStatement.setInt(1, saleProduct.getQuantity());
                    stockStatement.setLong(2, saleProduct.getProductId());
                    stockStatement.executeUpdate();

                    PreparedStatement saleProductStatement = connection.prepareStatement(CREATE_SALE_PRODUCTS);
                    saleProductStatement.setLong(1, saleId);
                    saleProductStatement.setLong(2, saleProduct.getProductId());
                    saleProductStatement.setInt(3, saleProduct.getQuantity());
                    saleProductStatement.executeUpdate();
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

        return res;
    }
    @Override
    public List<CategoryTotal> getSalesByCategory(Date dateStart, Date dateEnd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateEnd);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        dateEnd = calendar.getTime();
        List<CategoryTotal> categoryTotals = new ArrayList<>();
        List<String> categoryNames = jdbcTemplate.queryForList(GET_CATEGORY_NAMES, String.class);

        for (String categoryName : categoryNames) {
            Double totalAmount = jdbcTemplate.queryForObject(GET_SALES_BY_CATEGORY, new Object[]{categoryName, dateStart, dateEnd}, Double.class);

            if (totalAmount != null) {
                categoryTotals.add(new CategoryTotal(categoryName, totalAmount));
            }
        }

        return categoryTotals;
    }

    @Override
    public List<Sale> getSalesByDate(Date dateStart, Date dateEnd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateEnd);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        dateEnd = calendar.getTime();
        Object[] params = {dateStart, dateEnd};
        return jdbcTemplate.query(GET_SALES_BY_DATE, params, new SaleRowMapper());
    }

    @Override
    public Sale getSaleWithProductsById(long saleId) {
        return jdbcTemplate.query(GET_SALE_AND_PRODUCTS, new SaleWithProductsResultSetExtractor(), saleId);
    }

    @Override
    public MonthlyReport getSalesReport(Date dateStart, Date dateEnd) {
        final String GET_SALES_BY_MONTH = "SELECT SUM(sale_amount) AS totalAmount, SUM(sale_cost) AS totalCost " +
                "FROM Sales " +
                "WHERE sale_date BETWEEN ? AND ?";
        final String GET_PAYMENTS = "SELECT SUM(amount) AS payment_amount FROM Payments WHERE date BETWEEN ? AND ? AND payed = true";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateEnd);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        dateEnd = calendar.getTime();
        Double paymentAmount = jdbcTemplate.queryForObject(GET_PAYMENTS,new Object[]{dateStart, dateEnd}, new RowMapper<Double>() {
            @Override
            public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getDouble("payment_amount");
            }
        });
        MonthlyReport report =  jdbcTemplate.queryForObject(GET_SALES_BY_MONTH, new Object[]{dateStart, dateEnd}, new RowMapper<MonthlyReport>() {
            @Override
            public MonthlyReport mapRow(ResultSet rs, int rowNum) throws SQLException {
                double totalAmount = rs.getDouble("totalAmount");
                double totalCost = rs.getDouble("totalCost");
                return new MonthlyReport(totalAmount, totalCost);
            }
        });
        report.setPayments(paymentAmount);
        return report;
    }

    static class SaleWithProductsResultSetExtractor implements ResultSetExtractor<Sale> {
        @Override
        public Sale extractData(ResultSet rs) throws SQLException, DataAccessException {
            Sale sale = null;
            Map<Long, Sale> saleMap = new HashMap<>();

            while (rs.next()) {
                long saleId = rs.getLong("sale_id");

                if (sale == null) {
                    sale = new Sale();
                    sale.setId(saleId);
                    sale.setDate(rs.getTimestamp("sale_date"));
                    sale.setAmount(rs.getBigDecimal("sale_amount"));
                    sale.setCost(rs.getBigDecimal("sale_cost"));
                    sale.setSeller(rs.getString("seller"));
                    sale.setSaleProducts(new ArrayList<>());
                }

                SaleProduct saleProduct = new SaleProduct();
                saleProduct.setSaleId(saleId);
                saleProduct.setProductId(rs.getLong("product_id"));
                saleProduct.setQuantity(rs.getInt("quantity"));
                saleProduct.setProductName(rs.getString("product_name"));
                saleProduct.setProductDescription(rs.getString("product_description"));
                saleProduct.setProductPrice(rs.getString("product_price"));
                saleProduct.setProductCost(rs.getString("product_cost"));

                sale.getSaleProducts().add(saleProduct);
            }

            return sale;
        }
    }

    static class SaleRowMapper implements RowMapper<Sale> {
        @Override
        public Sale mapRow(ResultSet rs, int rowNum) throws SQLException {
            Sale sale = new Sale();
            sale.setId(rs.getLong("id"));
            sale.setDate(rs.getTimestamp("sale_date"));
            sale.setAmount(rs.getBigDecimal("sale_amount"));
            sale.setCost(rs.getBigDecimal("sale_cost"));
            sale.setSeller(rs.getString("seller"));

            return sale;
        }
    }
}
