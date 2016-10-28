<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<div class="product-filter">
    <div><h1>Замовлення</h1></div>
    <div class="sort">
        <div>
            <a class="button" href="<s:url value="/products"><s:param name="page" value="0"/></s:url>">На головну</a>
        </div>
    </div>
</div>
<section class="background address">
    <form:form action="/customer/order/create" modelAttribute="order" method="post" cssClass="flex-outer">
        <form:hidden path="customer.id"/>
        <div>
            <div>І'мя та прізвище:</div>
            <div>${order.customer.firstName.concat(" ").concat(order.customer.secondName)}</div>
        </div>
        <div>
            <div>Ел. пошта:</div>
            <div>${order.customer.email}</div>
        </div>
        <div>
            <div>Телефон:</div>
            <div>${order.customer.phone}</div>
        </div>
        <div>
            <form:label path="street" for="street">Вулиця<sup style="font-size: 1.5em">*</sup></form:label>
            <form:input path="street" type="text" id="street"/>
            <form:errors path="street" id="street" cssClass="error"/>
        </div>
        <div>
            <form:label path="city" for="city">Місто<sup style="font-size: 1.5em">*</sup></form:label>
            <form:input path="city" type="text" id="city"/>
            <form:errors path="city" id="city" cssClass="error"/>
        </div>
        <div>
            <form:label path="postcode" for="postcode">Індекс<sup style="font-size: 1.5em">*</sup></form:label>
            <form:input path="postcode" type="text" id="postcode"/>
            <form:errors path="postcode" id="postcode" cssClass="error"/>
        </div>
        <div>
            <form:label path="message" for="message">Додаткова інформація</form:label>
            <form:textarea path="message" rows="5" id="message"/>
            <form:errors path="message" id="message" cssClass="error"/>
        </div>
        <div>
            <form:button type="submit">Відіслати</form:button>
        </div>
        <div>
            <p style="font-size: 0.5em"><sup style="font-size: 1.5em">*</sup>Обов'язкова інформація</p>
        </div>
    </form:form>
</section>
