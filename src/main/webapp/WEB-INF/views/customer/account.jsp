<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.santex.entity.Status" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<sec:authorize access="hasRole('ROLE_USER')">
    <div class="product-filter">
        <div><h1>
            <c:choose>
                <c:when test="${not empty details.firstName}">
                    ${details.firstName}&nbsp;${details.secondName}
                </c:when>
                <c:otherwise>
                    ${details.email}
                </c:otherwise>
            </c:choose>
        </h1></div>
        <div class="sort">
            <c:if test="${not empty details.phone}">
                Телефон: ${details.phone}<br>
            </c:if>
            Ел. пошта: ${details.email}
        </div>
        <div class="sort">
            <c:if test="${not empty details.firstName}">
                І'мя: ${details.firstName}<br>
            </c:if>
            <c:if test="${not empty details.secondName}">
                Прізвище: ${details.secondName}
            </c:if>
        </div>
        <div class="sort">
            <div>
                <a class="button green-button" href="<s:url value="/customer/update"/>">Змінити</a>
            </div>
        </div>
    </div>
    <c:if test="${edited eq true}">
        <div class="edited">
            Данні профайла змінено.
        </div>
    </c:if>
    <section class="background">
        <c:choose>
            <c:when test="${empty orders}">
                <span style="text-align: center">Ви ще немаєте жодного замовлення.</span>
            </c:when>
            <c:otherwise>
                <table class="order">
                    <thead>
                    <tr>
                        <th>Номер</th>
                        <th>Адреса</th>
                        <th>Дата замовлення</th>
                        <th>Статус</th>
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
                            <td class="table-td-centring">
                                <c:set var="done" value="<%=Status.Виконано%>"/>
                                <c:set var="new_order" value="<%=Status.Нове%>"/>
                                <c:choose>
                                    <c:when test="${order.status eq done}">
                                        <span style="background: lawngreen; color: #666; border-radius: 3px; padding: 6px">Виконано</span>
                                    </c:when>
                                    <c:when test="${order.status eq new_order}">
                                        <span style="background: #ff1a1a; color: #fff; border-radius: 3px; padding: 6px">Нове замовлення</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="background: yellow; color: #666; border-radius: 3px; padding: 6px">Обробляється...</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><a href="<s:url value="/customer/showOrder=${order.id}"/>">Показати</a></td>
                            <td><a href="<s:url value="/customer/downloadPdf=${order.id}"/>">Завантажити</a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </section>
</sec:authorize>