package com.santex.dto;

import com.santex.entity.Brand;
import com.santex.entity.Category;
import com.santex.entity.Currency;
import com.santex.entity.Subcategory;

import java.util.List;

public class SearchCriteriaAdminProduct {
    private String search = "";
    private Sorting srt = Sorting.name_asc;
    private Boolean avail = null;
    private Boolean pr_vis = null;
    private Boolean disc_pr = null;
    private Currency cur = null;
    private int brandId;
    private List<Brand> brands;
    private int catId;
    private List<Category> categories;
    private int subId;
    private List<Subcategory> subcategories;

    public enum Sorting {
        name_asc("Назва, А-Я"),
        name_desc("Назва, Я-А"),
        sku_asc("Артикул, А-Я"),
        sku_desc("Артикул, Я-А"),
        price_asc("Ціна, А-Я"),
        price_desc("Ціна, Я-А"),
        sales_asc("Продажи, А-Я"),
        sales_desc("Продажи, Я-А");
        private String value;

        Sorting(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Sorting getSrt() {
        return srt;
    }

    public void setSrt(Sorting srt) {
        this.srt = srt;
    }

    public Boolean getAvail() {
        return avail;
    }

    public void setAvail(Boolean avail) {
        this.avail = avail;
    }

    public Boolean getPr_vis() {
        return pr_vis;
    }

    public void setPr_vis(Boolean pr_vis) {
        this.pr_vis = pr_vis;
    }

    public Boolean getDisc_pr() {
        return disc_pr;
    }

    public void setDisc_pr(Boolean disc_pr) {
        this.disc_pr = disc_pr;
    }

    public Currency getCur() {
        return cur;
    }

    public void setCur(Currency cur) {
        this.cur = cur;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public int getSubId() {
        return subId;
    }

    public void setSubId(int subId) {
        this.subId = subId;
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }
}
