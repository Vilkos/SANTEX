<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="hasRole('ROLE_ADMIN')">
    <div class="product-filter">
        <div><h1>
            <c:choose>
                <c:when test="${not empty customer.firstName}">
                    ${customer.firstName}&nbsp;${details.secondName}
                </c:when>
                <c:otherwise>
                    ${customer.email}
                </c:otherwise>
            </c:choose>
        </h1></div>
        <div class="sort">
            <c:if test="${not empty customer.phone}">
                Телефон: ${customer.phone}<br>
            </c:if>
            Ел. пошта: ${customer.email}
        </div>
        <div class="sort">
            <c:if test="${not empty customer.firstName}">
                І'мя: ${customer.firstName}<br>
            </c:if>
            <c:if test="${not empty customer.secondName}">
                Прізвище: ${customer.secondName}
            </c:if>
        </div>
        <div class="sort">
            <div>
                <a class="button green-button" href="<s:url value="/admin/customer/all"><s:param name="page" value="0"/></s:url>">Всі клієнти</a>
            </div>
        </div>
    </div>
    <section class="background">
        <c:choose>
            <c:when test="${empty orders}">
                <span style="text-align: center; color: red">Кієнт немає жодного замовлення.</span>
            </c:when>
            <c:otherwise>
                <div class="datagrid">
                    <table>
                        <thead>
                        <tr>
                            <th>Номер</th>
                            <th>Адреса</th>
                            <th>Дата замовлення</th>
                            <th>Детально</th>
                            <th>Фактура (Pdf)</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${orders}" var="order">
                            <tr>
                                <td>${order.id}</td>
                                <td>${order.street}, ${order.city} ${order.postcode}</td>
                                <td>${order.showTimeOfOrder()}</td>
                                <td><a href="<s:url value="/admin/showOrder=${order.id}"/>">Показати</a></td>
                                <td><a href="<s:url value="/admin/downloadPdf=${order.id}"/>">Завантажити</a></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</sec:authorize>