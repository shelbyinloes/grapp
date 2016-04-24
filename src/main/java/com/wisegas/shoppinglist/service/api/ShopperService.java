package com.wisegas.shoppinglist.service.api;

import com.wisegas.shoppinglist.service.dto.ShopperDto;
import com.wisegas.shoppinglist.service.dto.ShoppingListDto;

public interface ShopperService {
   ShopperDto loadByEmail(String email);

   ShoppingListDto addList(String id, String name);
}