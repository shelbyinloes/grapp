package com.wisegas.grapp.storemanagement.service_impl.api_impl;

import com.wisegas.common.lang.annotation.ApplicationService;
import com.wisegas.common.lang.annotation.Transactional;
import com.wisegas.grapp.storemanagement.domain.repository.GrappStoreNodeItemRepository;
import com.wisegas.grapp.storemanagement.domain.value.GrappStoreNodeItemId;
import com.wisegas.grapp.storemanagement.service.api.GrappStoreNodeItemService;
import com.wisegas.grapp.storemanagement.service.dto.GrappStoreNodeItemDTOO;
import com.wisegas.grapp.storemanagement.service_impl.factory.GrappStoreNodeItemDTOOFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
@Transactional
@ApplicationService
public class GrappStoreNodeItemServiceImpl implements GrappStoreNodeItemService {

   private final GrappStoreNodeItemRepository grappStoreNodeItemRepository;

   @Inject
   public GrappStoreNodeItemServiceImpl(GrappStoreNodeItemRepository grappStoreNodeItemRepository) {
      this.grappStoreNodeItemRepository = grappStoreNodeItemRepository;
   }

   @Override
   public GrappStoreNodeItemDTOO get(String id) {
      return GrappStoreNodeItemDTOOFactory.createDTO(grappStoreNodeItemRepository.get(GrappStoreNodeItemId.fromString(id)));
   }
}
