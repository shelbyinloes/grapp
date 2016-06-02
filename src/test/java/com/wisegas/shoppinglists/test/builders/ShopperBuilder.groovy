package com.wisegas.shoppinglists.test.builders

import com.wisegas.common.lang.value.Email
import com.wisegas.common.test.entity.EntityBuilder
import com.wisegas.shoppinglists.domain.entity.Shopper;

class ShopperBuilder {

   static unique = 0

   static Shopper shopper() {
      unique++
      EntityBuilder.wrapBuilder(new Shopper(
         Email.fromString("Test${unique}@email.com"),
      )) as Shopper
   }
}