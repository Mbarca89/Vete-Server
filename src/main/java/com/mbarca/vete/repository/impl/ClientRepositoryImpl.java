package com.mbarca.vete.repository.impl;

import com.mbarca.vete.domain.Client;
import com.mbarca.vete.exceptions.ClientNotFoundException;
import com.mbarca.vete.repository.ClientRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ClientRepositoryImpl implements ClientRepository {
    private final String GET_CLIENT_BY_ID = "SELECT * FROM Clients WHERE id = ?";
    private final JdbcTemplate jdbcTemplate;

    public ClientRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer createClient(Client client) {
        String CREATE_CLIENT = "INSERT INTO clients (name, surname, phone, email, social, user_name) VALUES (?,?,?,?,?,?)";
        return jdbcTemplate.update(CREATE_CLIENT,
                client.getName(),
                client.getSurname(),
                client.getPhone(),
                client.getEmail(),
                client.getSocial(),
                client.getUserName()
        );
    }

    @Override
    public Client getClientById(Long clientId) {
        Object[] params = {clientId};
        int[] types = {1};
        return jdbcTemplate.queryForObject(GET_CLIENT_BY_ID, params, types, new ClientRowMapper());
    }

    @Override
    public Integer deleteClient(Long id) {
        String DELETE_CLIENT = "DELETE FROM Clients WHERE id = ?";
        return jdbcTemplate.update(DELETE_CLIENT, id);
    }

    @Override
    public List<Client> getClients() {
        String GET_ALL_CLIENTS = "SELECT * FROM Clients";
        return jdbcTemplate.query(GET_ALL_CLIENTS, new ClientRowMapper());
    }

    @Override
    public Integer editClient(Client client) throws ClientNotFoundException {
        Client editClient = new Client();

        Object[] params = {client.getId()};
        int[] types = {1};
        Client currentClient = jdbcTemplate.queryForObject(GET_CLIENT_BY_ID, params, types, new ClientRowMapper());

        if (currentClient == null) {
            throw new ClientNotFoundException("Cliente no encontrado!");
        }

        editClient.setName(currentClient.getName());
        if (!client.getName().isEmpty()) editClient.setName(client.getName());
        else editClient.setName(currentClient.getName());
        if (!client.getSurname().isEmpty()) editClient.setSurname(client.getSurname());
        else editClient.setSurname(currentClient.getSurname());
        if (!client.getPhone().isEmpty()) editClient.setPhone(client.getPhone());
        else editClient.setPhone(currentClient.getPhone());
        if (!client.getEmail().isEmpty()) editClient.setEmail(client.getEmail());
        else editClient.setEmail(currentClient.getEmail());
        if (!client.getSocial().isEmpty()) editClient.setSocial(client.getSocial());
        else editClient.setSocial(currentClient.getSocial());
        if (!client.getUserName().isEmpty()) editClient.setUserName(client.getUserName());
        else editClient.setUserName(currentClient.getUserName());

        String EDIT_CLIENT = "UPDATE clients SET name = ?, surname = ?, phone = ?, email = ?, social = ?, user_name = ? WHERE id = ?";
        return jdbcTemplate.update(EDIT_CLIENT, editClient.getName(), editClient.getSurname(), editClient.getPhone(), editClient.getEmail(), editClient.getSocial(), editClient.getUserName(), client.getId());
    }

    @Override
    public List<Client> getClientsByName(String searchTerm) {
        Object[] params = {"%" + searchTerm + "%"};
        String GET_CLIENTS_NY_NAME = "SELECT * FROM Clients WHERE LOWER(name) LIKE LOWER(?)";
        return jdbcTemplate.query(GET_CLIENTS_NY_NAME, params, new ClientRowMapper());
    }

    static class ClientRowMapper implements RowMapper<Client> {
        @Override
        public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
            Client client = new Client();
            client.setId(rs.getLong("id"));
            client.setName(rs.getString("name"));
            client.setSurname(rs.getString("surname"));
            client.setPhone(rs.getString("phone"));
            client.setEmail(rs.getString("email"));
            client.setSocial(rs.getString("social"));
            client.setUserName(rs.getString("user_name"));
            return client;
        }
    }
}
