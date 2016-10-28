<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="hasRole('ROLE_ADMIN')">
    <div class="product-filter">
        <div>
            <c:if test="${brand.id != 0}"><h2>Редагувати категорію</h2></c:if>
            <c:if test="${brand.id == 0}"><h2>Додати катерогію</h2></c:if>
        </div>
        <div class="sort">
            <div><a class="button green-button" href="<s:url value="/admin/brand/all"/>">Всі категорії</a></div>
        </div>
    </div>
    <section class="background">
        <form:form action="/admin/category/update" modelAttribute="category" method="post" cssClass="flex-outer">
            <div>
                <form:hidden id="categoryName" path="id"/>
                <form:label for="categoryName" path="categoryName">Назва</form:label>
                <form:input path="categoryName" id="categoryName"/>
                <form:errors id="categoryName" path="categoryName" cssClass="error"/>
            </div>
            <div>
                <form:button name="Submit">Підтвердити</form:button>
            </div>
        </form:form>
    </section>
</sec:authorize>

