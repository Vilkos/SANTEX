package com.santex.controller;

import com.santex.dto.SearchCriteriaAdmin;
import com.santex.validator.CustomerFormValidator;
import com.santex.dto.CustomerCreateFormDto;
import com.santex.entity.Customer;
import com.santex.entity.Order;
import com.santex.service.EmailService;
import com.santex.service.OrderService;
import com.santex.service.CustomerService;
import com.santex.service.implementation.CurrentCustomer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.ServletException;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@SessionAttributes(types = SearchCriteriaAdmin.class)
public class CustomerController {
    private final CustomerService customerService;
    private final EmailService emailService;
    private final OrderService orderService;
    private final CustomerFormValidator validator;

    public CustomerController(CustomerService customerService, EmailService emailService, OrderService orderService, CustomerFormValidator validator) {
        this.customerService = customerService;
        this.emailService = emailService;
        this.orderService = orderService;
        this.validator = validator;
    }

    @ModelAttribute("adminCriteria")
    public SearchCriteriaAdmin getCustomerSearchCriteria() {
        return new SearchCriteriaAdmin();
    }

    @GetMapping("/newCustomer")
    public String addCustomer(Model model) {
        model.addAttribute("form", new CustomerCreateFormDto());
        return "customer-new";
    }

    @GetMapping("/customer/update")
    public String redirectCustomerToEdit(Model model, Authentication authentication) {
        CurrentCustomer currentCustomer = (CurrentCustomer) authentication.getPrincipal();
        model.addAttribute("form", customerService.findFormById(currentCustomer.getId()));
        return "customer-update";
    }

    @PostMapping({"/customer/update", "/new"})
    public String handleCustomerCreateForm(@Valid @ModelAttribute("form") CustomerCreateFormDto form,
                                           BindingResult result,
                                           HttpServletRequest request,
                                           RedirectAttributes attributes) {
        validator.validate(form, result);
        if (result.hasErrors()) {
            return "customer-update";
        }
        try {
            if (form.getId() == 0) {
                Customer customer = customerService.create(form);
                emailService.sendRegistrationInfo(customer.getId());
                try {
                    request.login(form.getEmail(), form.getPassword());
                } catch (ServletException e) {
                    e.printStackTrace();
                }
                attributes.addFlashAttribute("registered", true);
            } else {
                customerService.edit(form);
                attributes.addFlashAttribute("edited", true);
                return "redirect:/customer/";
            }
        } catch (DataIntegrityViolationException e) {
            result.rejectValue("email", "email", "Клієнт з такою ел. адресою вже існує!");
            return "customer-update";
        }
        return "redirect:/products?page=0";
    }

    // for customer purposes

    @GetMapping("/customer/")
    public String customerView(Model model, Authentication authentication) {
        CurrentCustomer currentCustomer = (CurrentCustomer) authentication.getPrincipal();
        CustomerCreateFormDto form = customerService.findFormById(currentCustomer.getId());
        List<Order> orders = orderService.byCustomer(currentCustomer.getId());
        model.addAttribute("details", form);
        model.addAttribute("orders", orders);
        return "customer-account";
    }

    // for admin purposes

    @GetMapping("/admin/customer/account={id}")
    public String adminView(@PathVariable("id") int id, Model model) {
        model.addAttribute("customer", customerService.findById(id));
        model.addAttribute("orders", orderService.byCustomer(id));
        return "customer-byCustomer";
    }

    @GetMapping("/admin/customer/remove={id}")
    public String removeCustomer(@PathVariable("id") int id, HttpServletRequest request) {
        customerService.remove(id);
        return "redirect:/admin/customer/all?" + request.getQueryString();
    }

    @GetMapping("/admin/customer/all")
    public String showAllCustomers(@PageableDefault(size = 20) Pageable request,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> from,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> to,
                                   @ModelAttribute("adminCriteria") SearchCriteriaAdmin adminCriteria,
                                   Model model) {

        adminCriteria.setFrom(from.orElse(adminCriteria.getFrom()));
        adminCriteria.setTo(to.orElse(adminCriteria.getTo()));

        Page<Customer> page = customerService.findByCustomerAdmin(adminCriteria, request);

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("page", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);

        return "customer-all";
    }
}
