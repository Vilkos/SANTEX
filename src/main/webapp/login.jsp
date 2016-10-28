<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="isAnonymous()">
    <div class="enter">
        <form class="login" action="<s:url value="/login"/>" method="post">
            <div>
                <h1>Вхід</h1>
            </div>
            <div>
                <c:if test="${param.error eq true}">
                    <span style="color: red">Невірно введені данні!</span>
                </c:if>
                <input id="email" name="email" type="text" placeholder="Email" autofocus/>
                <input id="password" name="password" type="password" placeholder="Пароль"/>
                <label for="remember">
                    <input type="checkbox" name="remember" id="remember"/>
                    Запам'ятати
                </label>
                <button type="submit">Увійти</button>
                <div>
                    <span>Ще не зареєстровані? </span><span><a href="<s:url value="newCustomer"/>">Реєстрація</a></span>
                </div>
            </div>
        </form>
    </div>
</sec:authorize>
