<%@ page import="com.santex.entity.Status" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="product-filter">
    <div><h1>Редагування замовлення №: <span>${order.id}</span></h1></div>
    <div class="sort">
        <div>
            <a class="button green-button" href="<s:url value="/admin/order/all"><s:param name="page" value="0"/></s:url>">На замовлення</a>
        </div>
    </div>
</div>
<form:form action="/admin/entries/update" modelAttribute="order" method="post">
    <section class="background entries">
        <div class="datagrid">
            <table>
                <thead>
                <tr style="text-align: center">
                    <th style="text-align: left">Артикул</th>
                    <th style="text-align: left">Назва</th>
                    <th>Ціна/Один.</th>
                    <th>Кількість</th>
                    <th>Одиниця</th>
                    <th>Ціна</th>
                </tr>
                </thead>
                <tfoot>
                <tr>
                    <td colspan="5" style="text-align: right"><strong>Разом:</strong></td>
                    <td style="text-align: right"><strong>
                        <form:hidden path="total"/>
                        <fmt:setLocale value="uk_UA" scope="session"/>
                        <fmt:formatNumber value="${order.total}" type="currency" minFractionDigits="2"/></strong>
                    </td>
                </tr>
                </tfoot>
                <tbody>
                <c:forEach items="${order.entryList}" var="entry" varStatus="i">
                    <tr>
                        <td style="text-align: left"><form:hidden path="entryList[${i.index}].id"/>
                            <form:hidden path="entryList[${i.index}].SKU"/>
                                ${entry.SKU}
                        </td>
                        <td><form:hidden path="entryList[${i.index}].productName"/>
                                ${entry.productName}</td>
                        <td style="text-align: center">
                            <form:input path="entryList[${i.index}].priceUAH" placeholder="Price" size="10"/>
                        </td>
                        <td style="text-align: center">
                            <form:input path="entryList[${i.index}].quantity" placeholder="Quantity" size="5"/>
                        </td>
                        <td style="text-align: center"><form:hidden path="entryList[${i.index}].unit"/>
                                ${entry.unit}</td>
                        <td style="text-align: right">
                            <fmt:formatNumber value="${entry.subtotal}" type="currency" minFractionDigits="2"/>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <div class="fieldset">
                <c:set var="done" value="<%=Status.Виконано%>"/>
                <c:set var="new_order" value="<%=Status.Нове%>"/>
            <c:choose>
            <c:when test="${order.status eq done}">
            <div style="background: #ccffcc">
                </c:when>
                <c:when test="${order.status eq new_order}">
                <div style="background: #ffcccc">
                    </c:when>
                    <c:otherwise>
                    <div style="background: #ffffcc">
                        </c:otherwise>
                        </c:choose>
                        <div>
                            <form:label for="status" path="status">Статус замовлення:</form:label>
                            <form:select path="status" id="status">
                                <form:options items="${status.values}"/>
                            </form:select>
                        </div>
                        <div>
                            <form:button name="save" value="save">Зберігти</form:button>
                        </div>
                    </div>
                    <div>
                        <div>
                            <form:label for="SKU" path="SKU">Артикул:</form:label>
                            <form:input path="SKU" id="SKU"/>
                            <form:errors path="SKU" id="SKU" cssStyle="font-size: 12px; color: red"/>
                        </div>
                        <div>
                            <form:button name="update" value="update">Поновити</form:button>
                        </div>
                    </div>
                </div>
                <div class="fieldset">
                    <div>
                        <div>
                            <form:hidden path="customer.firstName"/>
                            <form:hidden path="customer.secondName"/>
                            <div>І'мя та прізвище:</div>
                            <div>${order.customer.firstName.concat(" ").concat(order.customer.secondName)}</div>
                        </div>
                        <div>
                            <form:hidden path="customer.email"/>
                            <div>Ел. пошта:</div>
                            <div>${order.customer.email}</div>
                        </div>
                        <div>
                            <form:hidden path="customer.phone"/>
                            <div>Телефон:</div>
                            <div>${order.customer.phone}</div>
                        </div>
                        <div>
                            <form:hidden path="timeOfOrder"/>
                            <div>Дата замовлення:</div>
                            <div>${order.getTimeOfOrder()}</div>
                        </div>
                    </div>
                    <div>
                        <div>
                            <form:hidden path="id"/>
                            <form:label for="street" path="street">Вулиця, №</form:label>
                            <form:input path="street" id="street" size="30"/>
                        </div>
                        <div>
                            <form:label for="city" path="city">Місто</form:label>
                            <form:input path="city" id="city" size="30"/>
                        </div>
                        <div>
                            <form:label for="postcode" path="postcode">Пош. індекс</form:label>
                            <form:input path="postcode" id="postcode" size="10"/>
                        </div>
                        <div>
                            <form:button name="saveSend" value="saveSend">Зберігти та відіслати клієнтові</form:button>
                        </div>
                    </div>
                </div>
    </section>
</form:form>
