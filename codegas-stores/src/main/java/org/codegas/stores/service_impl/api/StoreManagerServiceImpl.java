package org.codegas.stores.service_impl.api;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.codegas.commons.lang.annotation.ApplicationService;
import org.codegas.commons.lang.annotation.Transactional;
import org.codegas.commons.lang.spacial.GeoPoint;
import org.codegas.commons.lang.value.PrincipalName;
import org.codegas.stores.domain.entity.StoreManager;
import org.codegas.stores.domain.repository.StoreManagerRepository;
import org.codegas.stores.service.api.StoreManagerService;
import org.codegas.stores.service.dto.StoreDto;
import org.codegas.stores.service.dto.StoreManagerDto;
import org.codegas.stores.service_impl.factory.StoreDtoFactory;
import org.codegas.stores.service_impl.factory.StoreManagerDtoFactory;

@Named
@Singleton
@Transactional
@ApplicationService
public class StoreManagerServiceImpl implements StoreManagerService {

    private final StoreManagerRepository storeManagerRepository;

    @Inject
    public StoreManagerServiceImpl(StoreManagerRepository storeManagerRepository) {
        this.storeManagerRepository = storeManagerRepository;
    }

    @Override
    public StoreManagerDto create(PrincipalName name) {
        return StoreManagerDtoFactory.createDto(storeManagerRepository.add(new StoreManager(name)));
    }

    @Override
    public Optional<StoreManagerDto> find(PrincipalName name) {
        return storeManagerRepository.find(name).map(StoreManagerDtoFactory::createDto);
    }

    @Override
    public StoreDto addStore(PrincipalName managerName, String storeName, GeoPoint location) {
        return StoreDtoFactory.createDto(storeManagerRepository.get(managerName).addStore(storeName, location));
    }
}
