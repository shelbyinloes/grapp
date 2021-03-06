package org.codegas.shoppinglists.domain.value;

import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Embeddable;

import org.codegas.commons.lang.value.CodeName;

@Embeddable
public class Item {

    @Basic
    private String code;

    @Basic
    private String name;

    public Item(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public Item(CodeName codeName) {
        this.code = codeName.getCode();
        this.name = codeName.getName();
    }

    protected Item() {

    }

    public CodeName toCodeName() {
        return new CodeName(code, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || !getClass().equals(o.getClass())) {
            return false;
        }

        Item item = (Item) o;
        return Objects.equals(this.code, item.code) &&
            Objects.equals(this.name, item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name);
    }
}
