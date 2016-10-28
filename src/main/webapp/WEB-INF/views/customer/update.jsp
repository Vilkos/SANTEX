<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<sec:authorize access="hasRole('ROLE_USER') or isAnonymous()">
    <div class="enter">
        <form:form action="/customer/update" modelAttribute="form" method="post" cssClass="registration">
            <div>
                <h1>Данні клієнта</h1>
            </div>
            <div>
                <div>
                    <form:hidden path="id" id="id"/>
                    <form:input path="email" placeholder="Ел. пошта"/>
                    <form:errors path="email" cssClass="error"/>
                    <form:password path="password" placeholder="Пароль"/>
                    <form:errors path="password" cssClass="error"/>
                    <form:password path="passwordRepeated" placeholder="Повторити пароль"/>
                    <form:errors path="passwordRepeated" cssClass="error"/>
                </div>
                <div>
                    <form:input path="firstName" placeholder="І'мя"/>
                    <form:errors path="firstName" cssClass="error"/>
                    <form:input path="secondName" placeholder="Прізвище"/>
                    <form:errors path="secondName" cssClass="error"/>
                    <form:input path="phone" placeholder="Телефон"/>
                    <form:errors path="phone" cssClass="error"/>
                    <form:button name="submit">Підтвердити</form:button>
                </div>
            </div>
        </form:form>
    </div>
</sec:authorize>