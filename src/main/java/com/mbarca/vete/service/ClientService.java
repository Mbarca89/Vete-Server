package com.mbarca.vete.service;

import com.mbarca.vete.dto.request.ClientRequestDto;
import com.mbarca.vete.dto.response.ClientResponseDto;
import com.mbarca.vete.exceptions.ClientNotFoundException;
import com.mbarca.vete.exceptions.MissingDataException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface ClientService {
    String createClient (ClientRequestDto clientRequestDto) throws MissingDataException;
    String deleteClient (Long id);
    List<ClientResponseDto> getClients();
    ClientResponseDto getClientById(Long clientId);
    String editClient (ClientRequestDto clientRequestDto) throws MissingDataException, ClientNotFoundException;
}
