package com.mbarca.vete.repository;

import com.mbarca.vete.domain.Client;
import com.mbarca.vete.exceptions.ClientNotFoundException;

import java.util.List;

public interface ClientRepository {
    Integer createClient(Client client);
    Integer deleteClient(Long id);
    List<Client> getClients();
    List<Client> getClientsByName(String searchTerm);
    Client getClientById(Long clientId);
    Integer editClient(Client client) throws ClientNotFoundException;
}
