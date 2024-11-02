package com.santex.entity;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity(name = "unit")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "unit")
public class Unit implements Serializable {
    private static final long serialVersionUID = 1401838993852484797L;
    private int id;
    private String unitName;
    private List<Product> productList;

    public Unit(String unitName) {
        this.unitName = unitName;
    }

    public Unit() {
    }

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Size(min = 1, max = 10, message = "Назва одиниці має містити від 1 до 10 символів.")
    @Column (nullable = false, length = 10, unique = true)
    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "unit")
    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "id=" + id +
                ", unitName='" + unitName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Unit unit = (Unit) o;

        return id == unit.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
