package com.mbarca.vete.repository.impl;

import com.mbarca.vete.domain.Product;
import com.mbarca.vete.domain.Provider;
import com.mbarca.vete.repository.ProviderRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProviderRepositoryImpl implements ProviderRepository {
    private final String CREATE_PROVIDER = "INSERT INTO Providers (name, contact_name, phone) VALUES (?,?,?)";
    private final String GET_ALL_PROVIDERS = "SELECT * FROM Providers";
    private final String GET_PROVIDERS_NAMES = "SELECT name FROM Providers";
    private final String GET_PROVIDER_BY_NAME = "SELECT * FROM Providers WHERE UPPER(name) = UPPER(?)";

    private final JdbcTemplate jdbcTemplate;

    public ProviderRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer createProvider(Provider provider) {
        return jdbcTemplate.update(CREATE_PROVIDER,
                provider.getName(),
                provider.getContactName(),
                provider.getPhone()
        );

    }

    @Override
    public List<Provider> getAllProviders() {
        return jdbcTemplate.query(GET_ALL_PROVIDERS, new ProviderRowMapper());
    }

    @Override
    public List<String> getProvidersNames() {
        return jdbcTemplate.queryForList(GET_PROVIDERS_NAMES, String.class);
    }

    @Override
    public Provider getProviderByName(String name) {
        Object[] params = {name};
        int[] types = {1};
        return jdbcTemplate.queryForObject(GET_PROVIDER_BY_NAME, params, types, new ProviderRowMapper());
    }

    static class ProviderRowMapper implements RowMapper<Provider> {
        @Override
        public Provider mapRow(ResultSet rs, int rowNum) throws SQLException {
            Provider provider = new Provider();
            provider.setName(rs.getString("name"));
            provider.setContactName(rs.getString("contact_name"));
            provider.setPhone(rs.getString("phone"));
            return provider;
        }
    }
}
