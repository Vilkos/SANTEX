<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="hasRole('ROLE_ADMIN')">
    <div class="product-filter">
        <div><h1>Реквізити компанії</h1></div>
        <div class="sort">
            <div><a class="button green-button" href="<s:url value="/admin/order/all"><s:param name="page" value="0"/></s:url>">На головну</a></div>
        </div>
    </div>
    <section class="background">
        <form:form method="post" action="/admin/credentials/update" modelAttribute="credentials" cssClass="flex-outer">
            <div>
                <span>Адреса, телефон, ел. пошта</span>
            </div>
            <div>
                <form:label for="name" path="name">Назва компанії</form:label>
                <form:input path="name" id="name"/>
            </div>
            <div>
                <form:label for="street" path="address.street">Вулиця</form:label>
                <form:input path="address.street" id="street"/>
            </div>
            <div>
                <form:label for="number" path="address.number">Номер</form:label>
                <form:input path="address.number" id="number"/>
            </div>
            <div>
                <form:label for="city" path="address.city">Місто</form:label>
                <form:input path="address.city" id="city"/>
            </div>
            <div>
                <form:label for="postcode" path="address.postcode">Індекс</form:label>
                <form:input path="address.postcode" id="postcode"/>
            </div>
            <div>
                <form:label for="phone1" path="phone1">Телефон 1</form:label>
                <form:input path="phone1" id="phone1"/>
            </div>
            <div>
                <form:label for="phone2" path="phone2">Телефон 2</form:label>
                <form:input path="phone2" id="phone2"/>
            </div>
            <div>
                <form:label for="phone3" path="phone3">Телефон 3</form:label>
                <form:input path="phone3" id="phone3"/>
            </div>
            <div>
                <form:label for="email" path="email">Ел. пошта</form:label>
                <form:input path="email" id="email"/>
            </div>
            <div>
                <form:label for="taxcode" path="taxcode">Податковий код</form:label>
                <form:input path="taxcode" id="taxcode"/>
            </div>

            <div>
                <span>Реквізити рахунку</span>
            </div>
            <div>
                <form:label for="bankName" path="bankDetails.BankName">Назва банку</form:label>
                <form:input path="bankDetails.BankName" id="bankName"/>
            </div>
            <div>
                <form:label for="iban" path="bankDetails.IBAN">IBAN</form:label>
                <form:input path="bankDetails.IBAN" id="iban"/>
            </div>
            <div>
                <form:label for="swift" path="bankDetails.SWIFT">SWIFT</form:label>
                <form:input path="bankDetails.SWIFT" id="swift"/>
            </div>
            <div>
                <form:label for="shipmentInfo" path="shipmentInfo">Інформація про доставку</form:label>
                <form:textarea path="shipmentInfo" id="shipmentInfo" rows="6" cols="40"/>
            </div>
            <div>
                <form:label for="description" path="description">Опис діяльності компанії</form:label>
                <form:textarea path="description" id="description" rows="6" cols="40"/>
            </div>
            <div>
                <form:button name="Submit" type="submit">Підтвердити</form:button>
            </div>
        </form:form>
    </section>
</sec:authorize>
