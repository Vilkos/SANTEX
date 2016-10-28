<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="up" tagdir="/WEB-INF/tags" %>
<sec:authorize access="hasRole('ROLE_ADMIN')">
    <s:url var="firstUrl" value="/admin/customer/all">
        <up:urlParamsAdmin adminCriteria="${adminCriteria}"/>
        <s:param name="page" value="0"/>
    </s:url>
    <s:url var="lastUrl" value="/admin/customer/all">
        <up:urlParamsAdmin adminCriteria="${adminCriteria}"/>
        <s:param name="page" value="${page.totalPages - 1}"/>
    </s:url>
    <s:url var="prevUrl" value="/admin/customer/all">
        <up:urlParamsAdmin adminCriteria="${adminCriteria}"/>
        <s:param name="page" value="${currentIndex - 2}"/>
    </s:url>
    <s:url var="nextUrl" value="/admin/customer/all">
        <up:urlParamsAdmin adminCriteria="${adminCriteria}"/>
        <s:param name="page" value="${currentIndex + 1}"/>
    </s:url>
    <div class="product-filter">
        <div><h1>Клієнти</h1></div>
        <form:form action="${firstUrl}" modelAttribute="adminCriteria" method="get" cssClass="sort">
            <div class="collection-sort">
                <form:label for="from" path="from">Від:</form:label>
                <form:input path="from" id="from"/>
            </div>
            <div class="collection-sort">
                <form:label for="to" path="to">До:</form:label>
                <form:input path="to" id="to"/>
            </div>
            <div class="collection-sort">
                <form:label path="search">Пошук:</form:label>
                <div>
                    <form:input path="search" type="search"/>
                    <form:button>Знайти</form:button>
                </div>
            </div>
        </form:form>
    </div>
    <section class="datagrid">
        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th><span class="fa fa-envelope-o" aria-hidden="true">&nbsp;</span>Ел. пошта</th>
                <th>І'мя</th>
                <th>Прізвище</th>
                <th><span class="fa fa-mobile" aria-hidden="true">&nbsp;</span>Телефон</th>
                <th><span class="fa fa-calendar-o" aria-hidden="true">&nbsp;</span>Дата реєстрації</th>
                <th>Замовлення</th>
                <th></th>
            </tr>
            </thead>
            <tfoot>
            <tr>
                <td colspan="8">
                    <ul class="pagination" role="menubar" aria-label="Pagination">
                        <c:choose>
                            <c:when test="${currentIndex == 1}">
                                <li class="disabled"><a href="#"><span>First</span></a></li>
                                <li class="disabled"><a href="#"><span>Previous</span></a></li>
                            </c:when>
                            <c:otherwise>
                                <li><a href="${firstUrl}"><span>First</span></a></li>
                                <li><a href="${prevUrl}"><span>Previous</span></a></li>
                            </c:otherwise>
                        </c:choose>
                        <c:forEach var="i" begin="${beginIndex}" end="${endIndex}">
                            <s:url var="pageUrl" value="/admin/customer/all">
                                <up:urlParamsAdmin adminCriteria="${adminCriteria}"/>
                                <s:param name="page" value="${i - 1}"/>
                            </s:url>
                            <c:choose>
                                <c:when test="${i == currentIndex}">
                                    <li class="current"><a href="${pageUrl}"><s:message text="${i}"/></a></li>
                                </c:when>
                                <c:otherwise>
                                    <li><a href="${pageUrl}"><s:message text="${i}"/></a></li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:choose>
                            <c:when test="${currentIndex == page.totalPages}">
                                <li class="disabled"><a href="#"><span>Next</span></a></li>
                                <li class="disabled"><a href="#"><span>Last</span></a></li>
                            </c:when>
                            <c:otherwise>
                                <li><a href="${nextUrl}"><span>Next</span></a></li>
                                <li><a href="${lastUrl}"><span>Last</span></a></li>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </td>
            </tr>
            </tfoot>
            <tbody>
            <c:forEach var="temp" items="${page.content}">
                <tr>
                    <td class="table-td-centring">${temp.id}</td>
                    <td class="table-td-centring">${temp.email}</td>
                    <td class="table-td-centring">${temp.firstName}</td>
                    <td class="table-td-centring">${temp.secondName}</td>
                    <td class="table-td-centring">${temp.phone}</td>
                    <td class="table-td-centring">${temp.showRegistrationDate()}</td>
                    <td class="table-td-centring"><a href="<s:url value="/admin/customer/account=${temp.id}"/>">Показати</a></td>
                    <td class="table-td-centring">
                        <a href="<s:url value="/admin/customer/remove=${temp.id}"/>"><span
                                class="fa fa-trash-o fa-2x" aria-hidden="true"
                                style="color: red"></span></a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </section>
    <script src="<s:url value="/static/datepicker/moment.js"/>"></script>
    <script src="<s:url value="/static/datepicker/pikaday.js"/>"></script>
    <script>
        var picker = new Pikaday({
            field: document.getElementById('from'),
            format: 'YYYY-MM-DD',
            onSelect: function () {
                console.log(this.getMoment().format('Do MMMM YYYY'));
            }
        });
    </script>
    <script>
        var picker = new Pikaday({
            field: document.getElementById('to'),
            format: 'YYYY-MM-DD',
            onSelect: function () {
                console.log(this.getMoment().format('Do MMMM YYYY'));
            }
        });
    </script>
</sec:authorize>
