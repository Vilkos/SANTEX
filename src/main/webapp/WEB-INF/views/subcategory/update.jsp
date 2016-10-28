<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="hasRole('ROLE_ADMIN')">
    <div class="product-filter">
        <div>
            <c:if test="${subcategory.id != 0}"><h1>Редагувати підкатегорію</h1></c:if>
            <c:if test="${subcategory.id == 0}"><h1>Додати підкатерогію</h1></c:if>
        </div>
        <div class="sort">
            <div><a class="button green-button" href="<s:url value="/admin/subcategory/all"/>">Всі підкатегорії</a>
            </div>
        </div>
    </div>
    <section class="background">
        <form:form action="/admin/subcategory/update" modelAttribute="subcategory" method="post" cssClass="flex-outer">
            <div>
                <form:hidden id="subcategoryName" path="id"/>
                <form:label for="subcategoryName" path="subcategoryName">Назва</form:label>
                <form:input path="subcategoryName" id="subcategoryName"/>
                <form:errors id="subcategoryName" path="subcategoryName"/>
            </div>
            <div>
                <form:label for="category" path="category.id">Категорія</form:label>
                <form:select path="category.id" id="category">
                    <form:option value="0" label="...Вибрати..." id="category"/>
                    <form:options items="${category}" itemValue="id" itemLabel="categoryName" id="category"/>
                </form:select>
                <form:errors path="category.id" id="category"/>
            </div>
            <div>
                <form:button name="Submit">Підтвердити</form:button>
            </div>
        </form:form>
    </section>
</sec:authorize>
