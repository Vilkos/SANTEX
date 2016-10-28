<%@tag description="Url params template" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ attribute name="adminCriteria" required="true" type="com.santex.dto.SearchCriteriaAdmin"%>

<c:if test="${adminCriteria.from != null}"><s:param name="from" value="${adminCriteria.from}"/></c:if>
<c:if test="${adminCriteria.to != null}"><s:param name="to" value="${adminCriteria.to}"/></c:if>