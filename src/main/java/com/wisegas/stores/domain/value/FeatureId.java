package com.wisegas.stores.domain.value;

import com.wisegas.common.lang.value.AbstractId;

import javax.persistence.Basic;
import javax.persistence.Embeddable;

@Embeddable
public class FeatureId extends AbstractId {
   @Basic
   private String id;

   public static FeatureId generate() {
      return new FeatureId(generateValue());
   }

   public static FeatureId fromString(String string) {
      return new FeatureId(string);
   }

   protected FeatureId() {

   }

   private FeatureId(String id) {
      this.id = id;
   }

   @Override
   public Object idHash() {
      return id;
   }
}