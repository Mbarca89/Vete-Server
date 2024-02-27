package com.mbarca.vete.repository;

import com.mbarca.vete.domain.Provider;

import java.util.List;

public interface ProviderRepository {
    Integer createProvider (Provider provider);
    List<Provider> getAllProviders ();
    Provider getProviderByName(String name);
}
