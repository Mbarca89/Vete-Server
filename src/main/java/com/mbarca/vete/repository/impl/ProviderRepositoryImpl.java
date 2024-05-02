package com.mbarca.vete.repository.impl;

import com.mbarca.vete.domain.Product;
import com.mbarca.vete.domain.Provider;
import com.mbarca.vete.domain.User;
import com.mbarca.vete.exceptions.NotFoundException;
import com.mbarca.vete.exceptions.UserNotFoundException;
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
    private final String GET_PROVIDER_BY_ID = "SELECT * FROM Providers WHERE id = ?";
    private final String EDIT_PROVIDER = "UPDATE Providers SET name = ?, contact_name = ?, phone = ? WHERE id = ?";

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

    @Override
    public Integer editProvider(Provider newProvider) throws NotFoundException {
        Provider editProvider = new Provider();
        System.out.println(newProvider.getId());
        Object[] params = {newProvider.getId()};
        int[] types = {1};
        Provider currentProvider = jdbcTemplate.queryForObject(GET_PROVIDER_BY_ID, params, types, new ProviderRowMapper());

        if (currentProvider == null) {
            throw new NotFoundException("Proveedor no encontrado!");
        }
        editProvider.setName(currentProvider.getName());
        if (!newProvider.getName().isEmpty()) editProvider.setName(newProvider.getName()); else editProvider.setName(currentProvider.getName());
        if (!newProvider.getContactName().isEmpty()) editProvider.setContactName(newProvider.getContactName()); else editProvider.setContactName(currentProvider.getContactName());
        if (!newProvider.getPhone().isEmpty()) editProvider.setPhone(newProvider.getPhone()); else editProvider.setPhone(currentProvider.getPhone());


        return jdbcTemplate.update(EDIT_PROVIDER, editProvider.getName(), editProvider.getContactName(), editProvider.getPhone(), newProvider.getId());
    }

    static class ProviderRowMapper implements RowMapper<Provider> {
        @Override
        public Provider mapRow(ResultSet rs, int rowNum) throws SQLException {
            Provider provider = new Provider();
            provider.setId(rs.getLong("id"));
            provider.setName(rs.getString("name"));
            provider.setContactName(rs.getString("contact_name"));
            provider.setPhone(rs.getString("phone"));
            return provider;
        }
    }
}
