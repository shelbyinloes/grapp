package com.wisegas.user.test.builders

import com.wisegas.common.test.EntityBuilder
import com.wisegas.user.domain.entity.User

class UserBuilder {

   static unique = 0

   static User user() {
      unique++
      EntityBuilder.wrapBuilder(new User(
         "test${unique}@email.com",
         "Test User ${unique}",
         "<avatar ${unique}>"
      )) as User
   }
}