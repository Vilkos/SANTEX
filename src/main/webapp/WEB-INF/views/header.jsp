<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contact" value="${credentials}"/>
<div class="header-wrapper">
    <div vocab="http://schema.org/" typeof="HardwareStore">
        <span property="name" content="${contact.name}"></span>
        <sec:authorize access="hasRole('ROLE_USER') or isAnonymous()">
            <a property="url" content="${pageContext.request.localName}" href="<s:url value="/products/renew"/>">
                <img property="logo" src="<s:url value="/static/images/logo.svg"/>" alt="${contact.name}">
            </a>
        </sec:authorize>
        <sec:authorize access="hasRole('ROLE_ADMIN')">
            <a property="url" content="${pageContext.request.localName}" href="<s:url value="/admin/order/all"/>">
                <img property="logo" src="<s:url value="/static/images/logo.svg"/>" alt="${contact.name}">
            </a>
        </sec:authorize>
        <span property="description" content="${contact.description}"></span>
        <span property="telephone" content="${contact.phone1}"></span>
        <span property="telephone" content="${contact.phone2}"></span>
        <span property="telephone" content="${contact.phone3}"></span>
        <span property="email" content="${contact.email}"></span>
        <address property="address" typeof="PostalAddress">
            <span property="addressCountry" content="Ukraine"></span>
            <span property="addressLocality" content="${contact.address.city}"></span>
            <span property="postalCode" content="${contact.address.postcode}"></span>
            <span property="streetAddress" content="${contact.address.street} ${contact.address.number}"></span>
        </address>
    </div>
    <div><img src="<s:url value="/static/images/santex_for_home.svg"/>" alt="sanitary for home"></div>
    <div class="account-block">
        <div class="account">
            <sec:authorize access="isAnonymous()">
                <span><a href="<s:url value="/login"/>">Вхід</a></span>
                <span class="name"><a href="<s:url value="/newCustomer"/>">Реєстрація</a></span>
            </sec:authorize>
            <sec:authorize access="isAuthenticated()">
                <span><a href="<s:url value="/logout"/>">Вихід</a></span>
            </sec:authorize>
            <sec:authorize access="hasRole('ROLE_USER')">
            <span class="name">
                <a href="<s:url value="/customer/"/>"><sec:authentication property="name"/></a>
            </span>
            </sec:authorize>
            <sec:authorize access="hasRole('ROLE_ADMIN')">
            <span class="name">
                <a href="<s:url value="/admin/"/>">Вітаємо, Адміністратор!</a>
            </span>
            </sec:authorize>
        </div>
        <c:if test="${cart.amountOfItems > 0}">
            <div class="basket">
                <sec:authorize access="hasRole('ROLE_USER') or isAnonymous()">
                <span><a href="<s:url value="/cart/show"/>">Кошик</a>
                    <c:choose>
                        <c:when test="${cart.allowSale}">
                            <span class="circle green"><span class="number">${cart.amountOfItems}</span></span>
                        </c:when>
                        <c:when test="${!cart.allowSale}">
                            <span class="circle"><span class="number">${cart.amountOfItems}</span></span>
                        </c:when>
                    </c:choose>
                    </span>
                    <span>${cart.stringTotalPrice}&nbsp;грн</span>
                </sec:authorize>
            </div>
        </c:if>
    </div>
</div>