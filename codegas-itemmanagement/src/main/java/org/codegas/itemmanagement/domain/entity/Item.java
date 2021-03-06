package org.codegas.itemmanagement.domain.entity;

import org.codegas.commons.domain.entity.DomainEntity;
import org.codegas.commons.lang.value.CodeName;
import org.codegas.itemmanagement.domain.value.Code;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Item extends DomainEntity<Code> {

    @EmbeddedId
    private Code primaryCode;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Item superItem;

    @Column(unique = true)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "superItem")
    private List<Item> subItems = new ArrayList<>();

    public Item(Code code, String name) {
        this(null, code, name);
    }

    protected Item() {

    }

    private Item(Item superItem, Code primaryCode, String name) {
        this.primaryCode = primaryCode;
        setSuperItem(superItem);
        setName(name);
    }

    public List<Item> getLineage() {
        List<Item> lineage = new ArrayList<>();
        Item ancestor = this;
        lineage.add(ancestor);
        while (!ancestor.isGeneral()) {
            lineage.add(ancestor = ancestor.getSuperItem());
        }
        return lineage;
    }

    public CodeName toCodeName() {
        return new CodeName(getPrimaryCode().toString(), getName());
    }

    @Override
    public Code getId() {
        return getPrimaryCode();
    }

    public Code getPrimaryCode() {
        return primaryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Item> getSubItems() {
        return subItems;
    }

    public Item addSubItem(Code code, String name) {
        Item subItem = new Item(this, code, name);
        subItems.add(subItem);
        return subItem;
    }

    public void acceptSubItem(Item subItem) {
        subItem.makeGeneral();
        subItem.setSuperItem(this);
        subItems.add(subItem);
    }

    public void removeSubItem(Item subItem) {
        subItems.remove(subItem);
    }

    public void makeGeneral() {
        if (!isGeneral()) {
            superItem.removeSubItem(this);
            superItem = null;
        }
    }

    public boolean isGeneral() {
        return superItem == null;
    }

    public Item getSuperItem() {
        return superItem;
    }

    private void setSuperItem(Item superItem) {
        this.superItem = superItem;
    }
}
