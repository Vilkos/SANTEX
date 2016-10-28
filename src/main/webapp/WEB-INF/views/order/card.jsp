<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="hasRole('ROLE_USER')">
    <div class="product-filter">
        <div><h1>Замовлення №: ${details.id}</h1></div>
        <div class="sort">
            Адреса:<br>
                ${details.street}, ${details.city} ${details.postcode}
        </div>
        <div class="sort">
            <c:if test="${not empty details.message}">
                Коментар: ${details.message}
            </c:if>
        </div>
        <div class="sort">
            <div>
                <a class="button green-button" href="<s:url value="/customer/"/>">На профайл</a>
            </div>
        </div>
    </div>
    <section class="background">
        <table class="order">
            <thead>
            <tr>
                <th>Артикул</th>
                <th>Назва</th>
                <th>Ціна/один.</th>
                <th>Кількість</th>
                <th>Разом</th>
            </tr>
            </thead>
            <tfoot>
            <tr>
                <td style="text-align: left">Вартість замовлення:</td>
                <td colspan="3"></td>
                <td style="text-align: right"><strong><fmt:setLocale value="uk_UA" scope="session"/>
                    <fmt:formatNumber value="${total}" type="currency"
                                      minFractionDigits="2"/></strong></td>
            </tr>
            </tfoot>
            <tbody>
            <c:forEach var="order" items="${entries}">
                <tr>
                    <td>${order.SKU}</td>
                    <td style="text-align: left">${order.productName}</td>
                    <td><fmt:formatNumber value="${order.priceUAH}" type="currency" minFractionDigits="2"/>
                        / ${order.unit}</td>
                    <td>${order.quantity}</td>
                    <td style="text-align: right"><fmt:formatNumber value="${order.subtotal}" type="currency"
                                                                    minFractionDigits="2"/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </section>
</sec:authorize>
<sec:authorize access="hasRole('ROLE_ADMIN')">
    <div class="product-filter">
        <div><h1>Замовлення №: ${details.id}</h1></div>
        <div class="sort">
            Адреса:<br>
                ${details.street}, ${details.city} ${details.postcode}
        </div>
        <div class="sort">
            <c:if test="${not empty details.message}">
                Коментар: ${details.message}
            </c:if>
        </div>
        <div class="sort">
            <div>
                <a class="button green-button" href="<s:url value="/admin/order/all"><s:param name="page" value="0"/></s:url>">На замовлення</a>
            </div>
        </div>
    </div>
    <section class="background">
        <div class="datagrid">
            <table>
                <thead>
                <tr>
                    <th>Артикул</th>
                    <th>Назва</th>
                    <th>Ціна/один.</th>
                    <th>Кількість</th>
                    <th>Разом</th>
                </tr>
                </thead>
                <tfoot>
                <tr>
                    <td style="text-align: left">Вартість замовлення:</td>
                    <td colspan="3"></td>
                    <td style="text-align: right"><strong><fmt:setLocale value="uk_UA" scope="session"/>
                        <fmt:formatNumber value="${total}" type="currency"
                                          minFractionDigits="2"/></strong></td>
                </tr>
                </tfoot>
                <tbody>
                <c:forEach var="order" items="${entries}">
                    <tr>
                        <td>${order.SKU}</td>
                        <td style="text-align: left">${order.productName}</td>
                        <td><fmt:formatNumber value="${order.priceUAH}" type="currency" minFractionDigits="2"/>
                            / ${order.unit}</td>
                        <td>${order.quantity}</td>
                        <td style="text-align: right"><fmt:formatNumber value="${order.subtotal}" type="currency"
                                                                        minFractionDigits="2"/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </section>
</sec:authorize>
