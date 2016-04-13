package com.wisegas.grapp.storemanagement.domain_impl.repository

import com.wisegas.common.persistence.jpa.impl.GenericRepositoryImplIntegrationTest
import com.wisegas.grapp.storemanagement.domain.entity.NodeItem
import com.wisegas.grapp.storemanagement.test.builders.GrappStoreNodeItemBuilder

class StoreNodeItemRepositoryImplIntegrationTest extends GenericRepositoryImplIntegrationTest<NodeItem> {

   @Override
   NodeItem createTestEntity() {
      return GrappStoreNodeItemBuilder.grappStoreNodeItem()
   }
}