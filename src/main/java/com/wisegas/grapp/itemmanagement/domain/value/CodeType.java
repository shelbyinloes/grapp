package com.wisegas.grapp.itemmanagement.domain.value;

public enum CodeType {
   GTIN("%013d"),
   UPC("%012d"),
   NACS("%02d%02d%02d"),
   MANUAL("%s"),
   RANDOM("%s");

   private final String valueFormat;

   public String getValueFormat() {
      return valueFormat;
   }

   CodeType(String valueFormat) {
      this.valueFormat = valueFormat;
   }
}