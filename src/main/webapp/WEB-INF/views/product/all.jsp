<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="up" tagdir="/WEB-INF/tags" %>
<sec:authorize access="hasRole('ROLE_USER') or isAnonymous()">
    <c:set var="root" value="${System.getProperty('catalina.home')}"/>
    <c:set var="context" value="${pageContext.request.contextPath}"/>

    <s:url var="firstUrl" value="">
        <up:urlParams criteria="${criteria}"/>
        <s:param name="page" value="0"/>
    </s:url>
    <s:url var="lastUrl" value="">
        <up:urlParams criteria="${criteria}"/>
        <s:param name="page" value="${page.totalPages - 1}"/>
    </s:url>
    <s:url var="prevUrl" value="">
        <up:urlParams criteria="${criteria}"/>
        <s:param name="page" value="${currentIndex - 2}"/>
    </s:url>
    <s:url var="nextUrl" value="">
        <up:urlParams criteria="${criteria}"/>
        <s:param name="page" value="${currentIndex + 1}"/>
    </s:url>

    <div class="product-filter">
        <h1>${criteria.sectionName}</h1>
        <form:form action="${firstUrl}" modelAttribute="criteria" method="get" cssClass="sort">
            <div class="collection-sort">
                <form:label path="srt">Сортування:</form:label>
                <c:if test="${criteria.catId gt 0 and criteria.subId eq 0}"><form:hidden path="catId"/></c:if>
                <c:if test="${criteria.catId eq 0 and criteria.subId gt 0}"><form:hidden path="subId"/></c:if>
                <form:select path="srt" onchange="this.form.submit()">
                    <form:option value="name_asc" label="Назва, А-Я"/>
                    <form:option value="name_desc" label="Назва, Я-А"/>
                    <form:option value="price_asc" label="Ціна, А-Я"/>
                    <form:option value="price_desc" label="Ціна, Я-А"/>
                    <form:option value="disc_price" label="Актуальні знижки"/>
                </form:select>
            </div>
            <div class="collection-sort">
                <form:label path="brandId">За брендом:</form:label>
                <form:select path="brandId" onchange="this.form.submit()">
                    <form:option value="0">...Всі бренди...</form:option>
                    <form:options items="${criteria.brandList}" itemValue="id" itemLabel="brandName"/>
                </form:select>
            </div>
            <div class="collection-sort">
                <form:label path="search">Пошук:</form:label>
                <div>
                    <form:input path="search" type="search"/>
                    <form:button name="Submit">Знайти</form:button>
                </div>
            </div>
        </form:form>
    </div>
    <c:if test="${created eq true}">
        <div class="added">
            Дякуємо за замовлення.
        </div>
    </c:if>
    <c:if test="${registered eq true}">
        <div class="added">
            Дякуємо за реєстрацію.
        </div>
    </c:if>
    <section class="products">
        <c:forEach items="${page.content}" var="item">
            <article class="product-card" vocab="http://schema.org/" typeof="Product">
                <meta property="brand" content="${item.brand.brandName}">
                <div class="product-name"><h5 property="name">${item.productName}</h5>
                </div>
                <div class="product-image">
                    <c:if test="${item.imageAvailability}">
                        <c:set var="imageUrl" value="/images/${item.SKU}/${item.SKU}_"/>
                        <img property="image" data-sizes="auto"
                             src="<s:url value="${context.concat(imageUrl).concat('400x400.jpg')}"/>"
                             srcset="data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw=="
                             data-srcset="
                             <s:url value="${context.concat(imageUrl).concat('400x400.jpg')}"/> 400w,
                         <s:url value="${context.concat(imageUrl).concat('300x300.jpg')}"/> 300w,
                         <s:url value="${context.concat(imageUrl).concat('500x500.jpg')}"/> 500w,
                         <s:url value="${context.concat(imageUrl).concat('600x600.jpg')}"/> 600w,
                         <s:url value="${context.concat(imageUrl).concat('700x700.jpg')}"/> 700w,
                         <s:url value="${context.concat(imageUrl).concat('800x800.jpg')}"/> 800w,
                         <s:url value="${context.concat(imageUrl).concat('900x900.jpg')}"/> 900w,
                         <s:url value="${context.concat(imageUrl).concat('1000x1000.jpg')}"/> 1000w,
                         <s:url value="${context.concat(imageUrl).concat('1100x1100.jpg')}"/> 1100w,
                         <s:url value="${context.concat(imageUrl).concat('1200x1200.jpg')}"/> 1200w,
                         <s:url value="${context.concat(imageUrl).concat('1300x1300.jpg')}"/> 1300w,
                         <s:url value="${context.concat(imageUrl).concat('1400x1400.jpg')}"/> 1400w,
                         <s:url value="${context.concat(imageUrl).concat('1500x1500.jpg')}"/> 1500w"
                             alt="Product Image" class="lazyload">
                    </c:if>
                    <c:if test="${item.imageAvailability eq false}">
                        <img src="<s:url value="/static/images/no_photo.svg"/>"
                             alt="Image is not available">
                    </c:if>
                    <span property="sku" class="SKU">Арт: ${item.SKU}</span>
                </div>
                <div class="product-info">
                    <c:choose>
                        <c:when test="${item.priceVisibility == true && item.price != 0 && item.discountPrice == 0}">
                            <div property="offers" typeof="Offer">
                                <h6 property="price" content="${item.stringFinalPrice}">
                                    <span>${item.stringPrice}<meta property="priceCurrency" content="UAH"/><span>&nbsp;грн</span></span>
                                </h6>
                            </div>
                        </c:when>
                        <c:when test="${item.discountPrice > 0 && item.priceVisibility == true && item.price != 0}">
                            <div class="discount" property="offers" typeof="Offer">
                                <h6>
                                    <span class="discountPrice" property="price"
                                          content="${item.stringFinalPrice}">${item.stringDiscountPrice}<meta property="priceCurrency" content="UAH"/><span>&nbsp;грн</span></span><br>
                                    <span class="previousPrice">${item.stringPrice}<span>&nbsp;грн</span></span>
                                </h6>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div>
                                <h6>
                                    <span class="requestPrice">За запитом</span>
                                </h6>
                            </div>
                        </c:otherwise>
                    </c:choose>
                    <div>
                        <a href="<s:url value="/cart/addToCart=${item.id}">
                      <up:urlParams criteria="${criteria}"/>
                      <s:param name="page" value="${currentIndex - 1}"/>
                      </s:url>"
                           class="button"><span>Купити</span></a>
                    </div>
                </div>
            </article>
        </c:forEach>
    </section>
    <nav class="pagination-wrapper">
        <ul class="pagination" role="menubar" aria-label="Pagination">
            <c:choose>
                <c:when test="${currentIndex == 1}">
                    <li class="disabled"><a href="#"><span>First</span></a></li>
                    <li class="disabled"><a href="#"><span>Previous</span></a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="${firstUrl}"><span>First</span></a></li>
                    <li><a href="${prevUrl}" rel="prev"><span>Previous</span></a></li>
                </c:otherwise>
            </c:choose>
            <c:forEach var="i" begin="${beginIndex}" end="${endIndex}">
                <s:url var="pageUrl" value="">
                    <up:urlParams criteria="${criteria}"/>
                    <s:param name="page" value="${i - 1}"/>
                </s:url>
                <c:choose>
                    <c:when test="${i == currentIndex}">
                        <li class="current"><a href="${pageUrl}"><s:message text="${i}"/></a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="${pageUrl}"><s:message text="${i}"/></a></li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:choose>
                <c:when test="${currentIndex == page.totalPages}">
                    <li class="disabled"><a href="#"><span>Next</span></a></li>
                    <li class="disabled"><a href="#"><span>Last</span></a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="${nextUrl}" rel="next"><span>Next</span></a></li>
                    <li><a href="${lastUrl}"><span>Last</span></a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </nav>
</sec:authorize>
