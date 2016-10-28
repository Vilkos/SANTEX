<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="hasRole('ROLE_USER') or isAnonymous()">
    <div class="advertise">
        <c:forEach var="item" items="${brandsForView}">
            <div>
                <c:choose>
                    <c:when test="${not empty item.url}">
                            <a href="${item.url}" target="_blank">
                                <img src="<s:url value="${pageContext.request.contextPath.concat('/brandLogos/').concat(item.id).concat('.svg')}"/>"
                                     alt="${item.brandName}">
                            </a>
                    </c:when>
                    <c:otherwise>
                        <img src="<s:url value="${pageContext.request.contextPath.concat('/brandLogos/').concat(item.id).concat('.svg')}"/>"
                             alt="${item.brandName}">
                    </c:otherwise>
                </c:choose>
            </div>
        </c:forEach>
    </div>
</sec:authorize>
<div class="developer">
    <small>&copy;
        <span property="copyrightYear">2016</span>
        <span property="copyrightHolder">Eugen Zubenko</span>
    </small>
</div>
