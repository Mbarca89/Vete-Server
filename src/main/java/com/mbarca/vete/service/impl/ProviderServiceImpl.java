package com.mbarca.vete.service.impl;

import com.mbarca.vete.domain.Provider;
import com.mbarca.vete.domain.User;
import com.mbarca.vete.dto.request.ProviderRequestDto;
import com.mbarca.vete.dto.request.UserRequestDto;
import com.mbarca.vete.dto.response.ProviderResponseDto;
import com.mbarca.vete.dto.response.UserResponseDto;
import com.mbarca.vete.exceptions.MissingDataException;
import com.mbarca.vete.exceptions.NotFoundException;
import com.mbarca.vete.repository.ProviderRepository;
import com.mbarca.vete.service.ProviderService;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;

    public ProviderServiceImpl(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    @Override
    public String createProvider(ProviderRequestDto providerRequestDto) throws MissingDataException {
        if(providerRequestDto.getName() == null) {
            throw new MissingDataException("Faltan datos!");
        }
        Provider provider = mapDtoToProvider(providerRequestDto);
        Integer response = providerRepository.createProvider(provider);

        if (response.equals(0)){
            return "Error al crear el proveedor!";
        }
        return "Proveedor creado correctamente!";
    }

    @Override
    public List<ProviderResponseDto> getAllProviders() {
        List<Provider> providers = providerRepository.getAllProviders();
        return providers.stream().map(this::mapProviderToDto).collect(Collectors.toList());
    }

    @Override
    public List<String> getProvidersNames() {
        return providerRepository.getProvidersNames();
    }

    @Override
    public ProviderResponseDto getProviderByName(String name) throws MissingDataException {
        if(name == null || name.isEmpty()) {
            throw new MissingDataException("Faltan datos!");
        }
        Provider provider = providerRepository.getProviderByName(name);
        return mapProviderToDto(provider);
    }

    @Override
    public String editProvider (ProviderRequestDto providerRequestDto) throws MissingDataException, NotFoundException {
        if (providerRequestDto.getName() == null || Objects.equals(providerRequestDto.getName(), "")){
            throw new MissingDataException("Faltan datos!");
        }

        Provider provider = mapDtoToProvider(providerRequestDto);
        Integer response = providerRepository.editProvider(provider);

        if (response.equals(0)) {
            return "Error al editar el proveedor!";
        }
        return "Proveedor editado correctamente!";
    }

    private Provider mapDtoToProvider(ProviderRequestDto providerRequestDto) {
        Provider provider = new Provider();
        provider.setId(providerRequestDto.getId());
        provider.setName(providerRequestDto.getName());
        provider.setContactName(providerRequestDto.getContactName());
        provider.setPhone(providerRequestDto.getPhone());
        return provider;
    }

    private ProviderResponseDto mapProviderToDto (Provider provider) {
        ProviderResponseDto providerResponseDto = new ProviderResponseDto();
        providerResponseDto.setId(provider.getId());
        providerResponseDto.setName(provider.getName());
        providerResponseDto.setContactName(provider.getContactName());
        providerResponseDto.setPhone(provider.getPhone());
        return providerResponseDto;
    }
}
