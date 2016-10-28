package com.santex.controller;

import com.santex.dto.ShoppingCart;
import com.santex.service.CompanyCredentialsService;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Set;

@Controller
public class HomeController {
    private final CompanyCredentialsService credentialsService;
    private final ShoppingCart cart;

    public HomeController(CompanyCredentialsService credentialsService, ShoppingCart cart) {
        this.credentialsService = credentialsService;
        this.cart = cart;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String root(HttpSession session) {
        session.setAttribute("cart", cart);
        return "redirect:/products";
    }

    @GetMapping("/welcome")
    public String welcome() {
        Set<String> roles = AuthorityUtils
                .authorityListToSet(SecurityContextHolder.getContext()
                        .getAuthentication().getAuthorities());
        if (roles.contains("ROLE_ADMIN")) {
            return "redirect:/admin/";
        }
        return "redirect:/customer/";
    }

    @GetMapping("/shipmentInfo")
    public String showShipmentInfo(Model model) {
        model.addAttribute("shipmentInfo", credentialsService.getCredentials().getShipmentInfo());
        return "admin-shipment";
    }

    @GetMapping("/admin/")
    public String admin() {
        return "redirect:/admin/order/all?page=0";
    }
}
