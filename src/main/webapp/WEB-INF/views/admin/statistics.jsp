<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="hasRole('ROLE_ADMIN')">
    <div class="product-filter">
        <div><h1>Статистика</h1></div>
        <div class="sort">
            <div><a class="button green-button" href="<s:url value="/admin/order/all"><s:param name="page" value="0"/></s:url>">На головну</a></div>
        </div>
    </div>
    <section class="background">
        <form:form method="get" action="/admin/statistic/update" modelAttribute="statistics" cssClass="flex-outer">
            <div>
                <span>В період з <time>${statistics.from}</time> по <time>${statistics.to}</time> було продано на</span>
            </div>
            <div>
                <h3 style="color: #5cbf2a; font-size: 2.5em; text-align: center; padding-top: 1em; padding-bottom: 2em">
                    <fmt:setLocale value="uk_UA" scope="session"/>
                    <fmt:formatNumber value="${statistics.sum}" type="currency" minFractionDigits="2"/>
                </h3>
            </div>
            <div>
                <span>Вибір періоду:</span>
            </div>
            <div>
                <form:label for="datepicker1" path="from">Від</form:label>
                <form:input path="from" placeholder="Date" id="from"/>
            </div>
            <div>
                <form:label for="datepicker2" path="to">До</form:label>
                <form:input path="to" placeholder="Date" id="to"/>
            </div>
            <div>
                <form:button>Показати</form:button>
            </div>
        </form:form>
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