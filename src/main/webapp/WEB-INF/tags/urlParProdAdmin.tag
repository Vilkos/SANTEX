<%@tag description="Url params template" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ attribute name="criteria" required="true" type="com.santex.dto.SearchCriteriaAdminProduct"%>

<c:if test="${criteria.search != ''}"><s:param name="search" value="${criteria.search}"/></c:if>
<c:if test="${criteria.srt.name() != 'name_asc'}"><s:param name="srt" value="${criteria.srt}"/></c:if>
<c:if test="${criteria.avail != null}"><s:param name="avail" value="${criteria.avail}"/></c:if>
<c:if test="${criteria.pr_vis != null}"><s:param name="pr_vis" value="${criteria.pr_vis}"/></c:if>
<c:if test="${criteria.disc_pr != null}"><s:param name="disc_pr" value="${criteria.disc_pr}"/></c:if>
<c:if test="${criteria.cur != null}"><s:param name="cur" value="${criteria.cur}"/></c:if>
<c:if test="${criteria.brandId gt 0}"><s:param name="brandId" value="${criteria.brandId}"/></c:if>
<c:if test="${criteria.catId gt 0 and criteria.subId eq 0}"><s:param name="catId" value="${criteria.catId}"/></c:if>
<c:if test="${criteria.catId eq 0 and criteria.subId gt 0}"><s:param name="subId" value="${criteria.subId}"/></c:if>
