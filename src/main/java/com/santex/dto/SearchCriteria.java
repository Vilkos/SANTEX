package com.santex.dto;

import com.santex.entity.Brand;

import java.util.List;

public class SearchCriteria {
    private String search;
    private String sectionName;
    private Sorting srt;
    private int brandId;
    private int catId;
    private int subId;

    private List<Brand> brandList;

    public SearchCriteria() {
        search = "";
        sectionName = "Каталог";
        srt = Sorting.name_asc;
    }

    public enum Sorting {
        name_asc,
        name_desc,
        price_asc,
        price_desc,
        disc_price
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

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        if (catId != this.catId) {
            this.brandId = 0;
            this.srt = Sorting.name_asc;
            this.subId = 0;
            this.catId = catId;
        }
    }

    public int getSubId() {
        return subId;
    }

    public void setSubId(int subId) {
        if (subId != this.subId) {
            this.brandId = 0;
            this.srt = Sorting.name_asc;
            this.catId = 0;
            this.subId = subId;
        }
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public List<Brand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Brand> brandList) {
        this.brandList = brandList;
    }

    public void clearCriteria() {
        this.search = "";
        this.srt = Sorting.name_asc;
        this.brandId = 0;
        this.catId = 0;
        this.subId = 0;
        this.sectionName = "Каталог";
    }
}
