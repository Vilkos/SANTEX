<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="hasRole('ROLE_ADMIN')">
    <div class="product-filter">
        <div><h1>Курси валют</h1></div>
        <div class="sort">
            <div><a class="button green-button" href="<s:url value="/admin/order/all"><s:param name="page" value="0"/></s:url>">На головну</a></div>
        </div>
    </div>
    <form:form method="post" action="/admin/currencies/update" modelAttribute="currencies">
        <section class="background">
            <div class="fieldset">
                <div>
                    <div>
                        <form:label for="usd" path="USDRateFromDb">USD</form:label>
                        <form:input path="USDRateFromDb" id="usd"/>
                    </div>
                    <div>
                        <form:label for="eur" path="EURRateFromDb">EUR</form:label>
                        <form:input path="EURRateFromDb" id="eur"/>
                    </div>
                    <div>
                        <form:button name="Submit" type="submit">Підтвердити</form:button>
                    </div>
                </div>
                <div>
                    <div>
                        <form:label for="autoUpdateCurrency" path="autoUpdateCurrency">Автооновлення курсів</form:label>
                        <form:checkbox path="autoUpdateCurrency" id="autoUpdateCurrency"/>
                    </div>
                    <div>
                        <form:label for="updateCurrencyUrl" path="updateCurrencyUrl">Лінк для оновлення курсів (JSON формат)</form:label>
                        <form:input path="updateCurrencyUrl" id="updateCurrencyUrl"/>
                    </div>
                    <div>Актуальні валютні курси з URL:</div>
                    <div>
                        <div>USD:</div>
                        <div>${currencies.USDRateFromUrl}</div>
                        <form:hidden path="USDRateFromUrl"/>
                    </div>
                    <div>
                        <div>EUR:</div>
                        <div>${currencies.EURRateFromUrl}</div>
                        <form:hidden path="EURRateFromUrl"/>
                    </div>
                </div>
            </div>
        </section>
    </form:form>
</sec:authorize>
