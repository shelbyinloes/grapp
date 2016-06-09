package com.wisegas.shoppinglists.service.dto;

import com.wisegas.common.lang.value.AbstractDto;

import java.util.List;

public class ShoppingListDto extends AbstractDto {

   private String name;
   private List<ShoppingListItemDto> items;

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public List<ShoppingListItemDto> getItems() {
      return items;
   }

   public void setItems(List<ShoppingListItemDto> items) {
      this.items = items;
   }
}
