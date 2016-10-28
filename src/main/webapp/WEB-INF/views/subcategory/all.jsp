<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="hasRole('ROLE_ADMIN')">
    <div class="product-filter">
        <div><h1>Підкатегорії</h1></div>
        <div class="sort">
            <div><a class="button green-button" href="<s:url value="/admin/subcategory/new"/>">Додати</a></div>
        </div>
    </div>
    <c:if test="${added eq true}">
        <div class="added">
            Підкатегорію "${info.subcategoryName}" до категорії "${info.category.categoryName}" додано.
        </div>
    </c:if>
    <c:if test="${edited eq true}">
        <div class="edited">
            Підкатегорію "${info.subcategoryName}" в категорії "${info.category.categoryName}" змінено.
        </div>
    </c:if>
    <c:if test="${removed eq true}">
        <div class="removed">
            Підкатегорію видалено.
        </div>
    </c:if>
    <section class="background">
        <div class="datagrid">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Назва підкатегорії</th>
                    <th>Категорія</th>
                    <th>Опції</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="temp" items="${subcategories}">
                    <tr>
                        <td>${temp.id}</td>
                        <td>${temp.subcategoryName}</td>
                        <td>${temp.category.categoryName}</td>
                        <td>
                            <a href="<s:url value="/admin/subcategory/edit=${temp.id}"/>"><span
                                    class="fa fa-pencil-square-o fa-2x" aria-hidden="true"
                                    style="color: darkorange">&nbsp;</span></a>
                            <a href="<s:url value="/admin/subcategory/remove=${temp.id}"/>"><span
                                    class="fa fa-trash-o fa-2x" aria-hidden="true"
                                    style="color: red"></span></a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </section>
</sec:authorize>
