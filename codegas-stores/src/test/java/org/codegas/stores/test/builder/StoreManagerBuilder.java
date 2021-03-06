package org.codegas.stores.test.builder;

import org.codegas.commons.lang.value.PrincipalName;
import org.codegas.stores.domain.entity.StoreManager;

public final class StoreManagerBuilder {

   private static int unique = 0;

   private StoreManagerBuilder() {

   }

   public static StoreManager build() {
      unique++;
      return new StoreManager(
         PrincipalName.fromString("test" + unique + "@email.com")
      );
   }
}
