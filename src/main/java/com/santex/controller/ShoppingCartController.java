package com.santex.controller;

import com.santex.dto.ProductCustomerDto;
import com.santex.dto.ShoppingCart;
import com.santex.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ProductService productService;
    private final ShoppingCart cart;

    public ShoppingCartController(ProductService productService, ShoppingCart cart) {
        this.productService = productService;
        this.cart = cart;
    }

    @GetMapping("/addToCart={id}")
    public String addItem(@PathVariable int id, HttpServletRequest request, HttpSession session) {
        if (request.getRequestedSessionId() != null && !request.isRequestedSessionIdValid()) {
            session.setAttribute("cart", cart);
        }
        ProductCustomerDto productCustomerDto = productService.findByIdForCustomer(id);
        cart.add(productCustomerDto);
        return "redirect:/products?" + request.getQueryString();
    }

    @GetMapping("/add={id}")
    public String add(@PathVariable("id") int id) {
        cart.add(productService.findByIdForCustomer(id));
        return "redirect:/cart/show";
    }

    @GetMapping("/remove={id}")
    public String remove(@PathVariable("id") int id) {
        if (cart.remove(id)) {
            return "redirect:/products/renew";
        } else {
            return "redirect:/cart/show";
        }
    }

    @GetMapping("/show")
    public String show() {
        return "cart-cart";
    }

    @GetMapping("/clear")
    public String clear() {
        cart.clearBasket();
        return "redirect:/products/renew";
    }
}

