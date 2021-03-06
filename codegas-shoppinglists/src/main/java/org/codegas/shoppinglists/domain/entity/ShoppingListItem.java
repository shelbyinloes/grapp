package org.codegas.shoppinglists.domain.entity;

import org.codegas.commons.domain.entity.DomainEntity;
import org.codegas.shoppinglists.domain.value.Item;
import org.codegas.shoppinglists.domain.value.ShoppingListItemId;

import javax.persistence.*;

@Entity
public class ShoppingListItem extends DomainEntity<ShoppingListItemId> {

    @EmbeddedId
    private ShoppingListItemId id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, optional = false)
    private ShoppingList shoppingList;

    private Item item;

    private boolean obtained;

    public ShoppingListItem(ShoppingList shoppingList, Item item) {
        id = ShoppingListItemId.generate();
        setShoppingList(shoppingList);
        setItem(item);
    }

    protected ShoppingListItem() {

    }

    @Override
    public ShoppingListItemId getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public boolean isObtained() {
        return obtained;
    }

    public void setObtained(boolean obtained) {
        this.obtained = obtained;
    }

    private void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    private void setItem(Item item) {
        this.item = item;
    }
}
