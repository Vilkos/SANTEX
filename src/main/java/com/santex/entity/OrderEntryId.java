package com.santex.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
class OrderEntryId implements Serializable {
    private static final long serialVersionUID = -7364294257203080646L;
    protected int orders;
    protected int product;

    public OrderEntryId() {
    }

    OrderEntryId(int orders, int product) {
        this.orders = orders;
        this.product = product;
    }

    @Column(name = "order_id")
    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    @Column(name = "product_id")
    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderEntryId that = (OrderEntryId) o;

        if (orders != that.orders) return false;
        return product == that.product;

    }

    @Override
    public int hashCode() {
        int result = orders;
        result = 31 * result + product;
        return result;
    }
}

