package com.santex.entity;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity(name = "brand")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "brand")
public class Brand implements Serializable {
    private static final long serialVersionUID = -1365364432976887935L;
    private int id;
    private String brandName;
    private String url;
    private List<Product> productList;
    private boolean logoAvailability;
    private MultipartFile logo;

    public Brand(String brandName, String url) {
        this.brandName = brandName;
        this.url = url;
    }

    public Brand() {
    }

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column (nullable = false, unique = true)
    @Size(min = 3, max = 255, message = "Назва бренду має містити від 3 до 255 символів.")
    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Column
    @Size(max = 255, message = "Лінк не має перевищувати 255 символів.")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @OneToMany (fetch = FetchType.LAZY, mappedBy = "brand")
    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @Transient
    public boolean isLogoAvailability() {
        return logoAvailability;
    }

    public void setLogoAvailability(boolean logoAvailability) {
        this.logoAvailability = logoAvailability;
    }

    @Transient
    public MultipartFile getLogo() {
        return logo;
    }

    public void setLogo(MultipartFile logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", brandName='" + brandName + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Brand brand = (Brand) o;

        return id == brand.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
