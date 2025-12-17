package com.mbarca.vete.repository.impl;

import com.mbarca.vete.domain.AfipResponseObject;
import com.mbarca.vete.domain.Bill;
import com.mbarca.vete.domain.BillProduct;
import com.mbarca.vete.repository.BillRepository;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
public class BillRepositoryImpl implements BillRepository {
    JdbcTemplate jdbcTemplate;

    public BillRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer saveBill(Bill bill) {

        LocalDate currentDate = LocalDate.now();
        java.sql.Date sqlDate = java.sql.Date.valueOf(currentDate);
        return jdbcTemplate.execute((ConnectionCallback<Integer>) connection -> {
            connection.setAutoCommit(false);
            try {
                // Insert Bill
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO Bills (fecha, tipo, numero, tipo_documento, documento, nombre, importe_total, importe_no_gravado, importe_gravado, importe_iva, estado, cae, cae_fch_vto, errors, observations, condicion_iva_descripcion) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);



                ps.setDate(1, sqlDate);
                ps.setString(2, bill.getTipo());
                ps.setLong(3, bill.getNumero());
                ps.setLong(4, bill.getTipoDocumento());
                ps.setLong(5, bill.getDocumento());
                ps.setString(6, bill.getNombre());
                ps.setDouble(7, bill.getImporteTotal());
                ps.setDouble(8, bill.getImporteNoGravado());
                ps.setDouble(9, bill.getImporteGravado());
                ps.setDouble(10, bill.getImporteIva());
                ps.setString(11, bill.getEstado());
                ps.setString(12, bill.getCae());
                ps.setString(13, bill.getCaeFchVto());
                ps.setArray(14, connection.createArrayOf("VARCHAR", bill.getErrors().stream()
                        .map(error -> error.getCode() + ": " + error.getMsg())
                        .toArray(String[]::new)));
                ps.setArray(15, connection.createArrayOf("VARCHAR", bill.getObservations().stream()
                        .map(obs -> obs.getCode() + ": " + obs.getMsg())
                        .toArray(String[]::new)));
                ps.setString(16, bill.getCondicionIvaDescripcion());

                ps.executeUpdate();

                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    long billId = generatedKeys.getLong(1);

                    // Insert Bill Products
                    for (BillProduct bp : bill.getBillProducts()) {
                        saveBillProduct(connection, billId, bp);
                    }

                    connection.commit();
                    return 1;
                } else {
                    throw new SQLException("Failed to get bill ID, no keys were generated.");
                }
            } catch (SQLException e) {
                connection.rollback();
                throw new SQLException("Failed to save bill: " + e.getMessage(), e);
            } finally {
                connection.setAutoCommit(true);
            }
        });
    }

    @Override
    public Bill getBillById(Long id) {
        String sql = "SELECT * FROM Bills WHERE id = ?";
        Bill bill = jdbcTemplate.queryForObject(sql, new Object[]{id}, new BillRowMapper());

        String productSql = "SELECT * FROM BillsProducts WHERE bill_id = ?";
        List<BillProduct> products = jdbcTemplate.query(productSql, new Object[]{id}, new BillProductRowMapper());
        bill.setBillProducts(products);

        return bill;
    }

    @Override
    public List<Bill> getBillsByDate(Date startDate, Date endDate) {
        String sql = "SELECT * FROM Bills WHERE fecha BETWEEN ? AND ? ORDER BY id DESC";
        List<Bill> bills = jdbcTemplate.query(sql, new Object[]{startDate, endDate}, new BillOnlyRowMapper());

        return bills;
    }

    private void saveBillProduct(Connection connection, Long billId, BillProduct product) throws SQLException {
        String sql = "INSERT INTO BillsProducts (bill_id, product_id, bar_code, description, quantity, price, net_price, iva) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, billId);
            ps.setLong(2, product.getId());
            ps.setLong(3, product.getBarCode());
            ps.setString(4, product.getDescription());
            ps.setInt(5, product.getQuantity());
            ps.setDouble(6, product.getPrice());
            ps.setDouble(7, product.getNetPrice());
            ps.setDouble(8, product.getIva());

            ps.executeUpdate();
        }
    }

    public static class BillRowMapper implements RowMapper<Bill> {
        @Override
        public Bill mapRow(ResultSet rs, int rowNum) throws SQLException {
            Bill bill = new Bill();
            bill.setId(rs.getLong("id"));
            bill.setFecha(rs.getTimestamp("fecha"));
            bill.setTipo(rs.getString("tipo"));
            bill.setNumero(rs.getLong("numero"));
            bill.setTipoDocumento(rs.getInt("tipo_documento"));
            bill.setDocumento(rs.getLong("documento"));
            bill.setNombre(rs.getString("nombre"));
            bill.setImporteTotal(rs.getDouble("importe_total"));
            bill.setImporteNoGravado(rs.getDouble("importe_no_gravado"));
            bill.setImporteGravado(rs.getDouble("importe_gravado"));
            bill.setImporteIva(rs.getDouble("importe_iva"));
            bill.setEstado(rs.getString("estado"));
            bill.setCae(rs.getString("cae"));
            bill.setCaeFchVto(rs.getString("cae_fch_vto"));
            bill.setCondicionIvaDescripcion(rs.getString("condicion_iva_descripcion"));

            bill.setErrors(parseAfipResponseObjectArray(rs.getArray("errors")));
            bill.setObservations(parseAfipResponseObjectArray(rs.getArray("observations")));

            return bill;
        }
    }

    public static class BillOnlyRowMapper implements RowMapper<Bill> {

        @Override
        public Bill mapRow(ResultSet rs, int rowNum) throws SQLException {
            Bill bill = new Bill();
            bill.setId(rs.getLong("id"));
            bill.setFecha(rs.getTimestamp("fecha"));
            bill.setTipo(rs.getString("tipo"));
            bill.setNumero(rs.getLong("numero"));
            bill.setImporteTotal(rs.getDouble("importe_total"));
            bill.setEstado(rs.getString("estado"));
            return bill;
        }

    }

    private static List<AfipResponseObject> parseAfipResponseObjectArray(Array sqlArray) throws SQLException {
        if (sqlArray == null) {
            return new ArrayList<>();
        }

        Object[] array = (Object[]) sqlArray.getArray();
        List<AfipResponseObject> list = new ArrayList<>();

        for (Object entry : array) {
            if (entry instanceof String) {
                String[] parts = ((String) entry).split(": ", 2);
                if (parts.length == 2) {
                    AfipResponseObject obj = new AfipResponseObject();
                    obj.setCode(parts[0]);
                    obj.setMsg(parts[1]);
                    list.add(obj);
                }
            }
        }
        return list;
    }

    public class BillProductRowMapper implements RowMapper<BillProduct> {
        @Override
        public BillProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
            BillProduct product = new BillProduct();
            product.setId(rs.getLong("product_id"));
            product.setBarCode(rs.getLong("bar_code"));
            product.setDescription(rs.getString("description"));
            product.setQuantity(rs.getInt("quantity"));
            product.setPrice(rs.getDouble("price"));
            product.setNetPrice(rs.getDouble("net_price"));
            product.setIva(rs.getDouble("iva"));
            return product;
        }

    }
}

