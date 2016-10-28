package com.santex.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "order_entry")
public class OrderEntry implements Serializable {
    private static final long serialVersionUID = -5013719143146994777L;
    private OrderEntryId id;
    private Order orders;
    private Product product;
    private int priceUAH;
    private int quantity;

    public OrderEntry() {
    }

    public OrderEntry(Order o, Product p, int priceUAH, int quantity) {
        this.id = new OrderEntryId(o.getId(), p.getId());
        this.orders = o;
        this.product = p;
        this.priceUAH = priceUAH;
        this.quantity = quantity;
    }

    @EmbeddedId
    public OrderEntryId getId() {
        return id;
    }

    public int obtainOrderId() {
        return id.getOrders();
    }

    public void setId(OrderEntryId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    public Order getOrders() {
        return orders;
    }

    public void setOrders(Order orders) {
        this.orders = orders;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Column(nullable = false)
    public int getPriceUAH() {
        return priceUAH;
    }

    public void setPriceUAH(int priceUAH) {
        this.priceUAH = priceUAH;
    }

    @Column(nullable = false)
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int subtotal() {
        return quantity * priceUAH;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderEntry entry = (OrderEntry) o;

        return id != null ? id.equals(entry.id) : entry.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
