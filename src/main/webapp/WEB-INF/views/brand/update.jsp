<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="hasRole('ROLE_ADMIN')">
    <div class="product-filter">
        <div>
            <c:if test="${brand.id != 0}"><h1>Редагувати бренд</h1></c:if>
            <c:if test="${brand.id == 0}"><h1>Додати бренд</h1></c:if>
        </div>
        <div class="sort">
            <div><a class="button green-button" href="<s:url value="/admin/brand/all"/>">Всі бренди</a></div>
        </div>
    </div>
    <section class="background">
        <form:form action="/admin/brand/update" modelAttribute="brand" method="post" cssClass="flex-outer" enctype="multipart/form-data">
            <div>
                <form:hidden id="brandName" path="id"/>
                <form:label for="brandName" path="brandName">Назва</form:label>
                <form:input path="brandName" id="brandName"/>
                <form:errors id="brandName" path="brandName" cssClass="error"/>
            </div>
            <div>
                <form:label for="logo" path="logo">Вибрати файл</form:label>
                <form:input path="logo" id="logo" type="file"/>
            </div>
            <div>
                <form:label for="logoAvailability" path="logoAvailability">Наявність логотипу</form:label>
                <form:checkbox path="logoAvailability" id="logoAvailability"/>
            </div>
            <div>
                <form:label for="url" path="url">Посилання на його сайт</form:label>
                <form:input path="url" id="url"/>
                <form:errors path="url" id="url" cssClass="error"/>
            </div>
            <div>
                <form:button name="Submit">Підтвердити</form:button>
            </div>
        </form:form>
    </section>
</sec:authorize>
