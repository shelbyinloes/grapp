package com.wisegas.grapp.domain.value;

import com.wisegas.common.persistence.jpa.value.EntityID;

import javax.persistence.Basic;
import javax.persistence.Embeddable;

@Embeddable
public class GrappUserId extends EntityID {
   @Basic
   private String id;

   public static GrappUserId generate() {
      return new GrappUserId(generateValue());
   }

   public static GrappUserId fromString(String string) {
      return new GrappUserId(string);
   }

   protected GrappUserId() {

   }

   private GrappUserId(String id) {
      this.id = id;
   }

   @Override
   protected Object idHash() {
      return id;
   }
}
