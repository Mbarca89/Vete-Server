package com.mbarca.vete.service.impl;

import com.mbarca.vete.domain.Client;
import com.mbarca.vete.domain.User;
import com.mbarca.vete.dto.request.ClientRequestDto;
import com.mbarca.vete.dto.response.ClientResponseDto;
import com.mbarca.vete.exceptions.ClientNotFoundException;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.repository.ClientRepository;
import com.mbarca.vete.service.ClientService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public String createClient(ClientRequestDto clientRequestDto) throws MissingDataException {
        if (clientRequestDto.getName() == null || Objects.equals(clientRequestDto.getName(), "")) {
            throw new MissingDataException("Faltan datos!");
        }

        Client client = mapDtoToClient(clientRequestDto);
        Integer response = clientRepository.createClient(client);

        if (response.equals(0)){
            return "Error al crear el cliente!";
        }
        return "Cliente creado correctamente!";
    }

    @Override
    public String deleteClient(Long id) {
        Integer response = clientRepository.deleteClient(id);
        if (response.equals(0)) {
            throw new EmptyResultDataAccessException(1);
        }
        return "Cliente eliminado correctamente";
    }

    @Override
    public List<ClientResponseDto> getClients() {
        List<Client> clients = clientRepository.getClients();
        return clients.stream().map(this::mapClientToDto).collect(Collectors.toList());
    }

    @Override
    public String editClient(ClientRequestDto clientRequestDto) throws MissingDataException, ClientNotFoundException {
        if (clientRequestDto.getName() == null || Objects.equals(clientRequestDto.getName(), "")) {
            throw new MissingDataException("Faltan datos!");
        }

        Client client = mapDtoToClient(clientRequestDto);
        Integer response = clientRepository.editClient(client);

        if (response.equals(0)){
            return "Error al editar el cliente!";
        }
        return "Cliente editado correctamente!";
    }

    private Client mapDtoToClient(ClientRequestDto clientRequestDto) {
        Client client = new Client();
        client.setId(clientRequestDto.getId());
        client.setName(clientRequestDto.getName());
        client.setSurname(clientRequestDto.getSurname());
        client.setPhone(clientRequestDto.getPhone());
        System.out.println(client.toString());
        return client;
    }

    private ClientResponseDto mapClientToDto (Client client) {
        ClientResponseDto clientResponseDto = new ClientResponseDto();
        clientResponseDto.setId(client.getId());
        clientResponseDto.setName(client.getName());
        clientResponseDto.setSurname(client.getSurname());
        clientResponseDto.setPhone(client.getPhone());
        return clientResponseDto;
    }
}
