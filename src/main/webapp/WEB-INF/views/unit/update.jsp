<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="hasRole('ROLE_ADMIN')">
    <div class="product-filter">
        <div>
            <c:if test="${unit.id != 0}"><h1>Редагувати одиницю</h1></c:if>
            <c:if test="${unit.id == 0}"><h1>Додати одиницю</h1></c:if>
        </div>
        <div class="sort">
            <div><a class="button green-button" href="<s:url value="/admin/unit/all"/>">Всі одиниці</a></div>
        </div>
    </div>
    <section class="background">
        <form:form action="/admin/unit/update" modelAttribute="unit" method="post" cssClass="flex-outer">
                <div>
                    <form:hidden id="unitName" path="id"/>
                    <form:label for="unitName" path="unitName">Назва</form:label>
                    <form:input path="unitName" id="unitName"/>
                    <form:errors id="unitName" path="unitName"/>
                </div>
                <div>
                    <form:button name="Submit">Підтвердити</form:button>
                </div>
        </form:form>
    </section>
</sec:authorize>
