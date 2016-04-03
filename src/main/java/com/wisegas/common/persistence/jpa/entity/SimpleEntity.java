package com.wisegas.common.persistence.jpa.entity;

import com.wisegas.common.persistence.jpa.value.EntityId;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class SimpleEntity<T extends EntityId> {

   @Override
   public boolean equals(Object object) {
      return object != null && getClass().equals(object.getClass()) && hashCode() == object.hashCode();
   }

   @Override
   public int hashCode() {
      return getId().hashCode();
   }

   public abstract T getId();
}
