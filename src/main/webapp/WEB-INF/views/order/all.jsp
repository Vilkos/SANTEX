<%@ page import="com.santex.entity.Status" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="up" tagdir="/WEB-INF/tags" %>
<sec:authorize access="hasRole('ROLE_ADMIN')">
    <s:url var="firstUrl" value="/admin/order/all">
        <up:urlParamsAdmin adminCriteria="${orderCriteria}"/>
        <s:param name="page" value="0"/>
    </s:url>
    <s:url var="lastUrl" value="/admin/order/all">
        <up:urlParamsAdmin adminCriteria="${orderCriteria}"/>
        <s:param name="page" value="${page.totalPages - 1}"/>
    </s:url>
    <s:url var="prevUrl" value="/admin/order/all">
        <up:urlParamsAdmin adminCriteria="${orderCriteria}"/>
        <s:param name="page" value="${currentIndex - 2}"/>
    </s:url>
    <s:url var="nextUrl" value="/admin/order/all">
        <up:urlParamsAdmin adminCriteria="${orderCriteria}"/>
        <s:param name="page" value="${currentIndex + 1}"/>
    </s:url>
    <div class="product-filter">
        <div><h1>Замовлення</h1></div>
        <form:form action="${firstUrl}" modelAttribute="orderCriteria" method="get" cssClass="sort">
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
                <th class="table-td-centring">Номер</th>
                <th class="table-td-centring"><span class="fa fa-user" aria-hidden="true">&nbsp;</span>Клієнт</th>
                <th class="table-td-centring"><span class="fa fa-home" aria-hidden="true">&nbsp;</span>Адреса</th>
                <th class="table-td-centring"><span class="fa fa-clock-o" aria-hidden="true">&nbsp;</span>Дата замовлення</th>
                <th class="table-td-centring">Статус</th>
                <th class="table-td-centring"><span class="fa fa-info" aria-hidden="true">&nbsp;</span>Детально</th>
                <th class="table-td-centring"><span class="fa fa-file-pdf-o" aria-hidden="true">&nbsp;</span>Фактура</th>
                <th class="table-td-centring">Редагувати</th>
                <th class="table-td-centring"><span class="fa fa-trash-o" aria-hidden="true">&nbsp;</span>Видалити</th>
            </tr>
            </thead>
            <tfoot>
            <tr>
                <td colspan="9">
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
                            <s:url var="pageUrl" value="/admin/order/all">
                                <up:urlParamsAdmin adminCriteria="${orderCriteria}"/>
                                <s:param name="page" value="${i - 1}"/>
                            </s:url>
                            <c:choose>
                                <c:when test="${i == currentIndex}">
                                    <li class="current"><a href="${pageUrl}"><c:out value="${i}"/></a></li>
                                </c:when>
                                <c:otherwise>
                                    <li><a href="${pageUrl}"><c:out value="${i}"/></a></li>
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
                <c:choose>
                    <c:when test="${temp.isCurrentDate()}">
                        <tr style="background: #ccffcc">
                    </c:when>
                    <c:otherwise>
                        <tr>
                    </c:otherwise>
                </c:choose>
                <td class="table-td-centring">${temp.id}</td>
                <td>
                    <c:if test="${not empty temp.getCustomer().firstName or not empty temp.getCustomer().secondName}">
                        ${temp.getCustomer().firstName} ${temp.getCustomer().secondName}<br>
                    </c:if>
                        ${temp.getCustomer().email}
                </td>
                <td>${temp.street}, ${temp.city} ${temp.postcode}<br>
                    <c:if test="${not empty temp.message}">
                        <span style="color: red">Коментар: </span>${temp.message}
                    </c:if>
                </td>
                <td class="table-td-centring">${temp.showTimeOfOrder()}</td>
                <td class="table-td-centring">
                    <c:set var="done" value="<%=Status.Виконано%>"/>
                    <c:set var="new_order" value="<%=Status.Нове%>"/>
                    <c:choose>
                        <c:when test="${temp.status eq done}">
                                <span style="background: lawngreen; color: #666; border-radius: 3px; padding: 6px">Виконано</span>
                        </c:when>
                        <c:when test="${temp.status eq new_order}">
                            <span style="background: #ff1a1a; color: #fff; border-radius: 3px; padding: 6px">Нове замовлення</span>
                        </c:when>
                        <c:otherwise>
                            <span style="background: yellow; color: #666; border-radius: 3px; padding: 6px">Обробляється...</span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="table-td-centring">
                    <a href="<s:url value="/admin/showOrder=${temp.id}"/>">Показати</a>
                </td>
                <td class="table-td-centring">
                    <a href="<s:url value="/admin/downloadPdf=${temp.id}"/>">Завантажити</a>
                </td>
                <td class="table-td-centring">
                    <a href="<s:url value="/admin/order/edit=${temp.id}"/>"><span
                            class="fa fa-pencil-square-o fa-2x" aria-hidden="true"
                            style="color: darkorange"></span></a>
                </td>
                <td class="table-td-centring">
                    <a href="<s:url value="/admin/order/remove=${temp.id}"/>"><span
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
