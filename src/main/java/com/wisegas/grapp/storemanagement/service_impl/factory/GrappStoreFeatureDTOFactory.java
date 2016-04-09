package com.wisegas.grapp.storemanagement.service_impl.factory;

import com.wisegas.grapp.storemanagement.domain.entity.GrappStoreFeature;
import com.wisegas.grapp.storemanagement.service.dto.GrappStoreFeatureDTO;

public final class GrappStoreFeatureDTOFactory {

   public static GrappStoreFeatureDTO createDTO(GrappStoreFeature grappStoreFeature) {
      GrappStoreFeatureDTO grappStoreLayoutDTO = new GrappStoreFeatureDTO();
      grappStoreLayoutDTO.setId(grappStoreFeature.getId().toString());
      grappStoreLayoutDTO.setPolygon(grappStoreFeature.getPolygon());
      return grappStoreLayoutDTO;
   }

   private GrappStoreFeatureDTOFactory() {

   }
}