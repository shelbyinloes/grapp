package com.wisegas.grapp.service_impl.api

import com.wisegas.grapp.service.api.GrappStoreService
import com.wisegas.lang.GeoPoint
import com.wisegas.test.BaseIntegrationTest;

import javax.inject.Inject;

public class GrappStoreServiceImplIntegrationTest extends BaseIntegrationTest {

   @Inject
   private GrappStoreService grappStoreService

   def "We can create a Store through the Service"() {
      when:
      def result = grappStoreService.create("TEST STORE", new GeoPoint(1, 2))

      then:
      result.getId()
      result.getName() == "TEST STORE"
      result.getLocation() == new GeoPoint(1, 2)
   }
}