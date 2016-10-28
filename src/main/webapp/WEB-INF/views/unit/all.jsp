<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="hasRole('ROLE_ADMIN')">
    <div class="product-filter">
        <div><h1>Одиниці</h1></div>
        <div class="sort">
            <div><a class="button green-button" href="<s:url value="/admin/unit/new"/>">Додати</a></div>
        </div>
    </div>
    <c:if test="${added eq true}">
        <div class="added">
            Одиницю "${info.unitName}" додано.
        </div>
    </c:if>
    <c:if test="${edited eq true}">
        <div class="edited">
            Одиницю "${info.unitName}" змінено.
        </div>
    </c:if>
    <c:if test="${removed eq true}">
        <div class="removed">
            Одиницю видалено.
        </div>
    </c:if>
    <section class="background">
        <div class="datagrid">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Назва одиниці</th>
                    <th>Опції</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="temp" items="${units}">
                    <tr>
                        <td>${temp.id}</td>
                        <td>${temp.unitName}</td>
                        <td>
                            <a href="<s:url value="/admin/unit/edit=${temp.id}"/>"><span
                                    class="fa fa-pencil-square-o fa-2x" aria-hidden="true"
                                    style="color: darkorange">&nbsp;</span></a>
                            <a href="<s:url value="/admin/unit/remove=${temp.id}"/>"><span
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
