<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="contact" value="${credentials}"/>
<ul id="main-menu" class="sm sm-blue">
    <sec:authorize access="hasRole('ROLE_USER') or isAnonymous()">
        <li><a href="<s:url value="/products/renew"/>">Головна</a></li>
        <li><a href="#">Каталог</a>
            <ul>
                <c:forEach items="${menu}" var="category">
                    <c:choose>
                        <c:when test="${empty category.subcategoryList}">
                            <li><a href="<s:url value=""><s:param name="catId" value="${category.id}"/><s:param name="page" value="0"/></s:url>">${category.categoryName}</a></li>
                        </c:when>
                        <c:otherwise>
                            <li><a href="<s:url value=""><s:param name="catId" value="${category.id}"/><s:param name="page" value="0"/></s:url>">${category.categoryName}</a>
                                <ul>
                                    <c:forEach items="${category.subcategoryList}" var="subcategory">
                                        <li>
                                            <a href="<s:url value=""><s:param name="subId" value="${subcategory.id}"/><s:param name="page" value="0"/></s:url>">${subcategory.subcategoryName}</a>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </ul>
        </li>
        <li><a href="#">Контакти</a>
            <ul class="mega-menu">
                <li>
                    <!-- The mega drop down contents -->
                    <div style="width:400px;max-width:100%;">
                        <div style="padding:5px 24px;">
                            <c:if test="${not empty contact.name and not empty contact.address.street and not empty contact.address.number and not empty contact.address.city and not empty contact.address.postcode}">
                                <p class="address"><strong>${contact.name}</strong><br>
                                        ${contact.address.street} ${contact.address.number}<br>
                                        ${contact.address.city} ${contact.address.postcode}</p><br>
                            </c:if>
                            <c:if test="${not empty contact.phone1}">
                                <a style="font-size: 24px" href="tel:${contact.phone1}"><span class="fa fa-mobile"
                                                                                              aria-hidden="true"></span>&nbsp;&nbsp;${contact.phone1}
                                </a>
                            </c:if>
                            <c:if test="${not empty contact.phone2}">
                                <a style="font-size: 24px" href="tel:${contact.phone2}"><span class="fa fa-mobile"
                                                                                              aria-hidden="true"></span>&nbsp;&nbsp;${contact.phone2}
                                </a>
                            </c:if>
                            <c:if test="${not empty contact.phone3}">
                                <a style="font-size: 24px" href="tel:${contact.phone3}"><span class="fa fa-mobile"
                                                                                              aria-hidden="true"></span>&nbsp;&nbsp;${contact.phone3}
                                </a><br>
                            </c:if>
                            <c:if test="${not empty contact.email}">
                                <a style="font-size: 16px" href="mailto:${contact.email}"><span class="fa fa-envelope-o"
                                                                                                aria-hidden="true"></span>&nbsp;&nbsp;${contact.email}
                                </a>
                            </c:if>
                        </div>
                    </div>
                </li>
            </ul>
        </li>
        <li><a href="#">Доставка та Оплата</a>
            <ul class="mega-menu">
                <li>
                    <!-- The mega drop down contents -->
                    <div style="width:400px;max-width:100%;">
                        <div style="padding:5px 24px;">
                            <p>${contact.shipmentInfo}</p>
                        </div>
                    </div>
                </li>
            </ul>
        </li>
        <li><a href="<s:url value="/downloadPriceList"/>" download>Завантажити прайс</a></li>
    </sec:authorize>
    <sec:authorize access="hasRole('ROLE_USER')">
        <li><a href="<s:url value="/customer/"/>">Ваш аккаунт</a></li>
    </sec:authorize>
    <sec:authorize access="hasRole('ROLE_ADMIN')">
        <li><a href="#">Налаштування</a>
            <ul>
                <li><a href="<s:url value="/admin/currencies/edit"/>">Курси валют</a></li>
                <li><a href="<s:url value="/admin/credentials/edit"/>">Реквізити компанії</a></li>
                <li><a href="<s:url value="/admin/settings/edit"/>">Налаштування</a></li>
                <li><a href="<s:url value="/admin/statistic/show"/>">Статистика</a></li>
                <li><a href="<s:url value="/admin/downloadPriceList"/>" download>Завантажити прайс</a></li>
            </ul>
        </li>
        <li><a href="<s:url value="/admin/order/all"><s:param name="page" value="0"/></s:url>">Замовлення</a></li>
        <li><a href="<s:url value="/admin/customer/all"><s:param name="page" value="0"/></s:url>">Клієнти</a></li>
        <li><a href="<s:url value="/admin/product/all"><s:param name="page" value="0"/></s:url>">Товари</a></li>
        <li><a href="<s:url value="/admin/category/all"/>">Категорії</a></li>
        <li><a href="<s:url value="/admin/subcategory/all"/>">Підкатегорії</a></li>
        <li><a href="<s:url value="/admin/unit/all"/>">Одиниці</a></li>
        <li><a href="<s:url value="/admin/brand/all"/>">Бренди</a></li>
    </sec:authorize>
</ul>
