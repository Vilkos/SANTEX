package com.santex.entity;

import jakarta.validation.Valid;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity(name = "subcategory")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "subcategory")
public class Subcategory implements Serializable {
    private static final long serialVersionUID = 8068018293549741820L;
    private int id;
    private String subcategoryName;
    private Category category;
    private List<Product> productList;

    public Subcategory(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public Subcategory() {
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

    @Size(min = 3, max = 255, message = "Назва підкатегорії має містити від 3 до 255 символів.")
    @Column(nullable = false)
    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    @Valid
    @ManyToOne
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @OneToMany (fetch = FetchType.LAZY, mappedBy = "subcategory")
    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public String toString() {
        return "Subcategory{" +
                "id=" + id +
                ", subcategoryName='" + subcategoryName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subcategory that = (Subcategory) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
