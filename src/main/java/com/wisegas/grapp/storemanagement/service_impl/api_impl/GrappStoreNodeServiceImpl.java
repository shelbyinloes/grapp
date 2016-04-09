package com.wisegas.grapp.storemanagement.service_impl.api_impl;

import com.wisegas.common.lang.annotation.ApplicationService;
import com.wisegas.common.lang.annotation.Transactional;
import com.wisegas.grapp.storemanagement.domain.entity.GrappStoreNode;
import com.wisegas.grapp.storemanagement.domain.repository.GrappStoreNodeRepository;
import com.wisegas.grapp.storemanagement.domain.value.GrappStoreNodeId;
import com.wisegas.grapp.storemanagement.service.api.GrappStoreNodeService;
import com.wisegas.grapp.storemanagement.service.dto.GrappStoreNodeDTO;
import com.wisegas.grapp.storemanagement.service_impl.factory.GrappStoreNodeDTOFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
@Transactional
@ApplicationService
public class GrappStoreNodeServiceImpl implements GrappStoreNodeService {

   private final GrappStoreNodeRepository grappStoreNodeRepository;

   @Inject
   public GrappStoreNodeServiceImpl(GrappStoreNodeRepository grappStoreNodeRepository) {
      this.grappStoreNodeRepository = grappStoreNodeRepository;
   }

   @Override
   public GrappStoreNodeDTO get(String id) {
      return GrappStoreNodeDTOFactory.createDTO(grappStoreNodeRepository.get(GrappStoreNodeId.fromString(id)));
   }

   @Override
   public GrappStoreNodeDTO update(String id, String name) {
      GrappStoreNode grappStoreNode = grappStoreNodeRepository.get(GrappStoreNodeId.fromString(id));
      grappStoreNode.setName(name);
      return GrappStoreNodeDTOFactory.createDTO(grappStoreNode);
   }
}