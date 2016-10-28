package com.santex.dto;

import com.santex.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.*;

import static com.santex.service.PriceService.toStringWithLocale;
import static com.santex.service.PriceService.toStringWithoutCondition;
import static com.santex.service.PriceService.total;

@Component("cart")
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ShoppingCart {
    private List<OrderEntryDto> entryList = new ArrayList<>();
    private int amountOfItems;
    private double totalPrice;
    private String stringTotalPrice;
    private boolean allowSale;
    private double minSumOfOrder;
    private double restToMinSum;

    public void add(ProductCustomerDto e) {
        if (!e.isPriceVisibility() || e.getFinalPrice() == 0) return;
        if (entryList.stream().filter(en -> en.getId() == e.getId()).findFirst().isPresent()) {
            entryList.stream().filter(o -> o.getId() == e.getId()).forEach(o -> {
                o.setQuantity(o.getQuantity() + 1);
                o.setSubtotal(o.getSubtotal() + o.getPriceUAH());
                o.setStringSubtotal(toStringWithoutCondition(o.getSubtotal()));
            });
        } else {
            String price = toStringWithLocale(e.getFinalPrice());
            OrderEntryDto entry = new OrderEntryDto();
            entry.setId(e.getId());
            entry.setImageAvailability(e.isImageAvailability());
            entry.setSKU(e.getSKU());
            entry.setProductName(e.getProductName());
            entry.setPriceUAH(e.getFinalPrice());
            entry.setStringPriceUAH(price);
            entry.setQuantity(1);
            entry.setUnit(e.getUnit().getUnitName());
            entry.setSubtotal(e.getFinalPrice());
            entry.setStringSubtotal(price);
            entryList.add(entry);
        }
        amountOfItems = entryList.stream().mapToInt(OrderEntryDto::getQuantity).sum();
        totalPrice = total(entryList);
        stringTotalPrice = toStringWithoutCondition(totalPrice);
        allowSale();
    }

    public boolean remove(int id) {
        entryList.stream().filter(o -> o.getId() == id).forEach(o -> {
            o.setQuantity(o.getQuantity() - 1);
            o.setSubtotal(o.getSubtotal() - o.getPriceUAH());
            o.setStringSubtotal(toStringWithoutCondition(o.getSubtotal()));
        });
        entryList.removeIf(o -> o.getQuantity() == 0);
        amountOfItems = entryList.stream().mapToInt(OrderEntryDto::getQuantity).sum();
        totalPrice = total(entryList);
        stringTotalPrice = toStringWithoutCondition(totalPrice);
        allowSale();
        return entryList.isEmpty();
    }

    public int getAmountOfItems() {
        return amountOfItems;
    }

    public void setAmountOfItems(int amountOfItems) {
        this.amountOfItems = amountOfItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStringTotalPrice() {
        return stringTotalPrice;
    }

    public void setStringTotalPrice(String stringTotalPrice) {
        this.stringTotalPrice = stringTotalPrice;
    }

    public boolean isAllowSale() {
        return allowSale;
    }

    public void setAllowSale(boolean allowSale) {
        this.allowSale = allowSale;
    }

    public String getMinSumOfOrder() {
        return toStringWithLocale(minSumOfOrder);
    }

    public void setMinSumOfOrder(double minSumOfOrder) {
        this.minSumOfOrder = minSumOfOrder;
    }

    public String getRestToMinSum() {
        return toStringWithoutCondition(restToMinSum);
    }

    public void setRestToMinSum(double restToMinSum) {
        this.restToMinSum = restToMinSum;
    }

    private void allowSale() {
        if (totalPrice - minSumOfOrder >= 0) {
            restToMinSum = 0;
            allowSale = true;
        } else {
            restToMinSum = minSumOfOrder - totalPrice;
            allowSale = false;
        }
    }

    @Autowired
    public void setSettingsService(SettingsService settingsService) {
        minSumOfOrder = settingsService.getSettings().getMinSumOfOrder();
    }

    public void clearBasket() {
        entryList.clear();
        amountOfItems = 0;
        totalPrice = 0;
    }

    public List<OrderEntryDto> getEntryList() {
        return entryList;
    }
}
