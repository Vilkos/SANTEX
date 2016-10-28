<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="hasRole('ROLE_ADMIN')">
    <div class="product-filter">
        <div><h1>Помилка</h1></div>
        <div class="sort">
            <div><a class="button green-button" href="<s:url value="/admin/order/all"><s:param name="page" value="0"/></s:url>">На головну</a></div>
        </div>
    </div>
    <section class="background">
        <span class="error"><s:message text="${message}"/></span>
    </section>
</sec:authorize>

