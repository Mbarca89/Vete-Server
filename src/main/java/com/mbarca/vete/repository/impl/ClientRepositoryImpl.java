package com.mbarca.vete.repository.impl;

import com.mbarca.vete.domain.Client;
import com.mbarca.vete.exceptions.ClientNotFoundException;
import com.mbarca.vete.exceptions.UserNotFoundException;
import com.mbarca.vete.repository.ClientRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Repository
public class ClientRepositoryImpl implements ClientRepository {
    private final String CREATE_CLIENT = "INSERT INTO clients (name, surname, phone) VALUES (?,?,?)";
    private final String DELETE_CLIENT = "DELETE FROM Clients WHERE id = ?";
    private final String GET_ALL_CLIENTS = "SELECT * FROM Clients";
    private final String EDIT_CLIENT = "UPDATE clients SET name = ?, surname = ?, phone = ? WHERE id = ?";
    private final String GET_CLIENT_BY_ID = "SELECT * FROM Clients WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    public ClientRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer createClient(Client client) {
        return jdbcTemplate.update(CREATE_CLIENT,
                client.getName(),
                client.getSurname(),
                client.getPhone()
                );
    }

    @Override
    public Integer deleteClient(Long id) {
        return jdbcTemplate.update(DELETE_CLIENT, id);
    }

    @Override
    public List<Client> getClients() {
        return jdbcTemplate.query(GET_ALL_CLIENTS, new ClientRowMapper());
    }

    @Override
    public Integer editClient(Client client) throws ClientNotFoundException {
        Client editClient = new Client();

        Object[] params = {client.getId()};
        int[] types = {1};
        Client currentClient = jdbcTemplate.queryForObject(GET_CLIENT_BY_ID, params, types, new ClientRowMapper());

        if(currentClient == null) {
            throw new ClientNotFoundException("Cliente no encontrado!");
        }

        editClient.setName(currentClient.getName());
        if (!client.getName().isEmpty()) editClient.setName(client.getName()); else editClient.setName(currentClient.getName());
        if (!client.getSurname().isEmpty()) editClient.setSurname(client.getSurname()); else editClient.setSurname(currentClient.getSurname());
        if (!client.getPhone().isEmpty()) editClient.setPhone(client.getPhone()); else editClient.setPhone(currentClient.getPhone());

        return jdbcTemplate.update(EDIT_CLIENT, editClient.getName(), editClient.getSurname(), editClient.getPhone(), client.getId());
    }

    static class ClientRowMapper implements RowMapper<Client> {
        @Override
        public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
            Client client = new Client();
            client.setId(rs.getLong("id"));
            client.setName(rs.getString("name"));
            client.setSurname(rs.getString("surname"));
            client.setPhone(rs.getString("phone"));
            return client;
        }
    }
}
