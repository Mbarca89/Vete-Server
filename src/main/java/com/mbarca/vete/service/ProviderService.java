package com.mbarca.vete.service;

import com.mbarca.vete.dto.request.ProviderRequestDto;
import com.mbarca.vete.dto.response.ProviderResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.exceptions.NotFoundException;

import java.util.List;

public interface ProviderService {
    String createProvider (ProviderRequestDto provider) throws MissingDataException;
    List<ProviderResponseDto> getAllProviders ();
    List<String> getProvidersNames ();
    ProviderResponseDto getProviderByName(String name) throws MissingDataException;
    String editProvider (ProviderRequestDto providerRequestDto) throws MissingDataException, NotFoundException;
}
