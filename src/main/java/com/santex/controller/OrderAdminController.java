package com.santex.controller;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.zugferd.exceptions.DataIncompleteException;
import com.itextpdf.text.zugferd.exceptions.InvalidCodeException;
import com.itextpdf.xmp.XMPException;
import com.santex.dto.*;
import com.santex.entity.Order;
import com.santex.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

import static com.santex.service.PriceService.subtotal;
import static com.santex.service.PriceService.total;

@Controller
@RequestMapping("/admin")
@SessionAttributes(types = SearchCriteriaAdmin.class)
public class OrderAdminController {
    private final OrderService orderService;
    private final EmailService emailService;
    private final ProductService productService;

    public OrderAdminController(OrderService orderService, EmailService emailService, ProductService productService) {
        this.orderService = orderService;
        this.emailService = emailService;
        this.productService = productService;
    }

    @ModelAttribute("orderCriteria")
    public SearchCriteriaAdmin getOrderSearchCriteria() {
        return new SearchCriteriaAdmin();
    }

    @GetMapping("/order/all")
    public String getAllAdminPaged(@PageableDefault(size = 20) Pageable request,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> from,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> to,
                                   @ModelAttribute("orderCriteria") SearchCriteriaAdmin adminCriteria,
                                   Model model) {


        adminCriteria.setFrom(from.orElse(adminCriteria.getFrom()));
        adminCriteria.setTo(to.orElse(adminCriteria.getTo()));

        Page<Order> page = orderService.findByCriteriaAdmin(adminCriteria, request);

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("page", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        return "order-all";
    }

    @GetMapping("/order/edit={id}")
    public String editOrder(@PathVariable("id") int id, Model model) {
        OrderForm order = orderService.getById(id);
        model.addAttribute("order", order);
        return "order-entries";
    }

    @PostMapping(value = "/entries/update", params = "update")
    public String updateOrder(@ModelAttribute("order") OrderForm order, BindingResult result, Model model) throws IOException, XMPException, TransformerException, ParserConfigurationException, SAXException, ParseException, InvalidCodeException, DataIncompleteException, DocumentException {
        update(order, model, result);
        return "order-entries";
    }

    @PostMapping(value = "/entries/update", params = "save")
    public String save(@ModelAttribute("order") OrderForm order, BindingResult result, Model model) {
        update(order, model, result);
        orderService.edit(order);
        return "order-entries";
    }

    @PostMapping(value = "/entries/update", params = "saveSend")
    public String saveSend(@ModelAttribute("order") OrderForm order, BindingResult result, Model model) {
        update(order, model, result);
        orderService.edit(order);
        emailService.sendInvoice(order.getId());
        return "redirect:/admin/order/all?page=0";
    }

    private void update(OrderForm order, Model model, BindingResult result) {
        order.getEntryList().removeIf(q -> q.getQuantity() == 0);
        if (!order.getSKU().isEmpty()) {
            Optional<ProductCustomerDto> product = productService.findBySKU(order.getSKU());
            if (product.isPresent()) {
                if (order.getEntryList().stream().filter(p -> p.getId() == product.get().getId()).findFirst().isPresent()) {
                    ProductCustomerDto p = product.get();
                    order.getEntryList().stream().filter(en -> en.getId() == p.getId()).forEach(en -> en.setQuantity(en.getQuantity() + 1));
                    order.setSKU(null);
                } else {
                    ProductCustomerDto p = product.get();
                    OrderEntryDto entry = new OrderEntryDto();
                    entry.setId(p.getId());
                    entry.setSKU(p.getSKU());
                    entry.setProductName(p.getProductName());
                    entry.setPriceUAH(p.getFinalPrice());
                    entry.setQuantity(1);
                    entry.setUnit(p.getUnit().getUnitName());
                    entry.setSubtotal(p.getFinalPrice());
                    order.getEntryList().add(entry);
                    order.setSKU(null);
                }
            } else {
                order.setSKU(null);
                result.rejectValue("SKU", "SKU", "Товар за даним артикулом не знайдено.");
            }
        }

        order.getEntryList().forEach(oe -> {
            oe.setQuantity(oe.getQuantity());
            oe.setPriceUAH(oe.getPriceUAH());
            oe.setSubtotal(subtotal(oe));
        });

        order.setTotal(total(order.getEntryList()));
        order.setTimeOfOrder(order.getTimeOfOrder());
        order.setStreet(order.getStreet());
        order.setCity(order.getCity());
        order.setPostcode(order.getPostcode());
        order.setMessage(order.getMessage());
        order.setStatus(order.getStatus());

        model.addAttribute("order", order);
    }

    @GetMapping("/showByCustomer={id}")
    public String getAllByCustomer(@PathVariable("id") int id, Model model) {
        model.addAttribute("orders", orderService.byCustomer(id));
        return "order-byCustomer";
    }

    @GetMapping("/order/remove={id}")
    public String removeOrder(@PathVariable("id") int id) {
        orderService.remove(id);
        return "redirect:/admin/order/all?page=0";
    }

    @GetMapping("/downloadPriceList")
    public ModelAndView downloadAdminPriceList() {
        return new ModelAndView("adminView", "productList", productService.findAllForAdminPriceList());
    }
}
