package com.santex.controller;

import com.santex.dto.ProductAdminDto;
import com.santex.dto.SearchCriteriaAdminProduct;
import com.santex.entity.Currency;
import com.santex.entity.Product;
import com.santex.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.Optional;

import static com.santex.service.PriceService.toInteger;

@Controller
@RequestMapping("/admin/product")
@SessionAttributes(types = SearchCriteriaAdminProduct.class)
public class ProductAdminController {
    private final ProductService productService;
    private final ProductImageService productImageService;
    private final BrandService brandService;
    private final UnitService unitService;
    private final CategoryService categoryService;
    private final SubcategoryService subcategoryService;
    private DbCreationService dbCreationService;
    private CachePreheatService cachePreheatService;

    public ProductAdminController(ProductService productService, ProductImageService productImageService, BrandService brandService, UnitService unitService, CategoryService categoryService, SubcategoryService subcategoryService) {
        this.productService = productService;
        this.productImageService = productImageService;
        this.brandService = brandService;
        this.unitService = unitService;
        this.categoryService = categoryService;
        this.subcategoryService = subcategoryService;
    }

    @Lazy
    @Autowired
    public void setDbCreationService(DbCreationService dbCreationService) {
        this.dbCreationService = dbCreationService;
    }

    @Lazy
    @Autowired
    public void setCachePreheatService(CachePreheatService cachePreheatService) {
        this.cachePreheatService = cachePreheatService;
    }

    @ModelAttribute("criteriaAdmin")
    public SearchCriteriaAdminProduct getSearchCriteriaAdmin() {
        return new SearchCriteriaAdminProduct();
    }

    @GetMapping("/all")
    public String getAllAdminPaged(@PageableDefault(size = 50, sort = "productName") Pageable request,
                                   @RequestParam Optional<String> search,
                                   @RequestParam Optional<SearchCriteriaAdminProduct.Sorting> srt,
                                   @RequestParam Optional<Boolean> avail,
                                   @RequestParam Optional<Boolean> pr_vis,
                                   @RequestParam Optional<Boolean> disc_pr,
                                   @RequestParam Optional<Currency> cur,
                                   @RequestParam Optional<Integer> brandId,
                                   @RequestParam Optional<Integer> catId,
                                   @RequestParam Optional<Integer> subId,
                                   @ModelAttribute("criteriaAdmin") SearchCriteriaAdminProduct criteriaAdmin,
                                   Model model) {

        criteriaAdmin.setSearch(search.orElse(criteriaAdmin.getSearch()));
        criteriaAdmin.setSrt(srt.orElse(criteriaAdmin.getSrt()));
        criteriaAdmin.setAvail(avail.orElse(criteriaAdmin.getAvail()));
        criteriaAdmin.setPr_vis(pr_vis.orElse(criteriaAdmin.getPr_vis()));
        criteriaAdmin.setDisc_pr(disc_pr.orElse(criteriaAdmin.getDisc_pr()));
        criteriaAdmin.setCur(cur.orElse(criteriaAdmin.getCur()));
        criteriaAdmin.setBrandId(brandId.orElse(criteriaAdmin.getBrandId()));
        criteriaAdmin.setCatId(catId.orElse(criteriaAdmin.getCatId()));
        criteriaAdmin.setSubId(subId.orElse(criteriaAdmin.getSubId()));

        criteriaAdmin.setBrands(brandService.findByCriteriaAdmin(criteriaAdmin));
        criteriaAdmin.setCategories(categoryService.findByCriteriaAdmin(criteriaAdmin));
        criteriaAdmin.setSubcategories(subcategoryService.findByCriteriaAdmin(criteriaAdmin));

        Page<ProductAdminDto> page = productService.findByCriteriaAdmin(criteriaAdmin, request);

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("page", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        return "product-adminAll";
    }

    //ADDING NEW PRODUCT.......................

    @GetMapping("/new")
    public String newProduct(Model model) {
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("units", unitService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("product", new ProductAdminDto());
        return "product-update";
    }

    @PostMapping("/update")
    public String addProduct(@Valid @ModelAttribute("product") ProductAdminDto product,
                             BindingResult bindingResult,
                             RedirectAttributes attributes,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("brands", brandService.findAll());
            model.addAttribute("units", unitService.findAll());
            model.addAttribute("categories", categoryService.findAll());
            return "product-update";
        }

        if (product.getId() == 0) {
            productService.add(
                    product.getSKU(),
                    product.isAvailability(),
                    product.isPriceVisibility(),
                    toInteger(product.getPrice()),
                    toInteger(product.getDiscountPrice()),
                    product.getCurrency(),
                    product.getProductName(),
                    product.getImage(),
                    product.getSubcategory(),
                    product.getBrand(),
                    product.getUnit());
            attributes.addFlashAttribute("added", true);
        } else {
            productService.edit(
                    product.getId(),
                    product.getSKU(),
                    product.isAvailability(),
                    product.isPriceVisibility(),
                    toInteger(product.getPrice()),
                    toInteger(product.getDiscountPrice()),
                    product.getCurrency(),
                    product.getProductName(),
                    product.getImage(),
                    product.isImageAvailability(),
                    product.getSubcategory().getId(),
                    product.getBrand().getId(),
                    product.getUnit().getId());
            attributes.addFlashAttribute("edited", true);
        }
        attributes.addFlashAttribute("product", product);
        return "redirect:/admin/product/all?page=0";
    }

    //EDITING THE PRODUCT.......................


    @GetMapping("/edit={id}")
    public String redirectToProductEdit(@PathVariable("id") int id,
                                        Model model) {
        model.addAttribute("product", productService.findByIdForAdmin(id));
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("units", unitService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "product-update";
    }

    //REMOVING THE PRODUCT......................

    @GetMapping("/remove={id}")
    public String removeProduct(@PathVariable("id") int id,
                                HttpServletRequest request,
                                RedirectAttributes attributes) {
        Product product = productService.remove(id);
        attributes.addFlashAttribute("removed", true);
        attributes.addFlashAttribute("product", product);
        return "redirect:/admin/product/all?" + request.getQueryString();
    }

    @GetMapping("/db_creation")
    public String db_creation() {
        dbCreationService.run();
        return "redirect:/admin/product/all?page=0";
    }

    @GetMapping("/addPhotosInBatch")
    public String addingPhotosInBatch() {
        productImageService.addInBatch();
        return "redirect:/admin/product/all?page=0";
    }

    @GetMapping("/cachePreheat")
    public String cachePreheat() {
        cachePreheatService.run();
        return "redirect:/admin/product/all?page=0";
    }
}
