package com.santex.controller;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.zugferd.exceptions.DataIncompleteException;
import com.itextpdf.text.zugferd.exceptions.InvalidCodeException;
import com.itextpdf.xmp.XMPException;
import com.santex.dto.OrderEntryDto;
import com.santex.dto.ShoppingCart;
import com.santex.entity.Order;
import com.santex.service.CompanyCredentialsService;
import com.santex.service.EmailService;
import com.santex.service.OrderEntryService;
import com.santex.service.OrderService;
import com.santex.service.implementation.CurrentCustomer;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

import jakarta.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.santex.service.PriceService.total;

@Controller
public class OrderCustomerController {
    private static final Path priceList = Paths.get(System.getProperty("catalina.home"), "priceList", "priceList.xlsx");
    private final OrderService orderService;
    private final OrderEntryService orderEntryService;
    private final ShoppingCart cart;
    private final EmailService emailService;
    private final CompanyCredentialsService credentialsService;

    public OrderCustomerController(OrderService orderService, OrderEntryService orderEntryService, ShoppingCart cart, EmailService emailService, CompanyCredentialsService credentialsService) {
        this.orderService = orderService;
        this.orderEntryService = orderEntryService;
        this.cart = cart;
        this.emailService = emailService;
        this.credentialsService = credentialsService;
    }

    @GetMapping("/customer/order/new")
    public String newOrder(Model model, Authentication authentication) {
        CurrentCustomer currentCustomer = (CurrentCustomer) authentication.getPrincipal();
        Order order = new Order();
        order.setCustomer(currentCustomer.getCustomer());
        model.addAttribute("order", order);
        return "order-new";
    }

    @PostMapping("/customer/order/create")
    public String createOrder(@Valid @ModelAttribute("order") Order order, BindingResult bindingResult, RedirectAttributes attributes) throws IOException, XMPException, TransformerException, ParserConfigurationException, SAXException, ParseException, InvalidCodeException, DataIncompleteException, DocumentException {
        if (bindingResult.hasErrors()) {
            return "order-new";
        }

        orderService.add(order);

        List<OrderEntryDto> entryList = cart.getEntryList();
        for (OrderEntryDto entry : entryList) {
            orderEntryService.add(orderService.findById(order.getId()), entry.getId(), entry.getPriceUAH(), entry.getQuantity());
        }
        cart.clearBasket();
        emailService.sendInvoice(order.getId());
        attributes.addFlashAttribute("created", true);
        return "redirect:/products?page=0";
    }

    @GetMapping({"/customer/showOrder={id}", "/admin/showOrder={id}"})
    public String showOrderById(@PathVariable("id") int id, Model model) {
        Order order = orderService.findById(id);
        List<OrderEntryDto> entryList = orderEntryService.getByOrder(id);
        model.addAttribute("details", order);
        model.addAttribute("entries", entryList);
        model.addAttribute("total", total(entryList));
        return "order-card";
    }

    @GetMapping({"/customer/downloadPdf={id}", "/admin/downloadPdf={id}"})
    public ModelAndView downloadPdf(@PathVariable("id") int id) {
        Order order = orderService.findById(id);
        List<OrderEntryDto> entryList = orderEntryService.getByOrder(id);
        ModelAndView mav = new ModelAndView("pdfView", "entryList", entryList);
        mav.addObject("order", order);
        mav.addObject("companyCredentials", credentialsService.getCredentials());
        return mav;
    }

    @GetMapping("/downloadPriceList")
    public void downloadUserPriceList(HttpServletResponse response) {

        if (Files.exists(priceList)) {
            String ldt = LocalDateTime.now(ZoneId.of("Europe/Kiev")).format(DateTimeFormatter.ISO_DATE);
            String fileName =  credentialsService.getCredentials().getName() + "_" + ldt + ".xlsx";

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            try {
                Files.copy(priceList, response.getOutputStream());
                response.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
