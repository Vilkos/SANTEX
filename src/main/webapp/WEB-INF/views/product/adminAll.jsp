<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="up" tagdir="/WEB-INF/tags" %>
<sec:authorize access="hasRole('ROLE_ADMIN')">

    <s:url var="firstUrl" value="/admin/product/all">
        <up:urlParProdAdmin criteria="${criteriaAdmin}"/>
        <s:param name="page" value="0"/>
    </s:url>
    <s:url var="lastUrl" value="/admin/product/all">
        <up:urlParProdAdmin criteria="${criteriaAdmin}"/>
        <s:param name="page" value="${page.totalPages - 1}"/>
    </s:url>
    <s:url var="prevUrl" value="/admin/product/all">
        <up:urlParProdAdmin criteria="${criteriaAdmin}"/>
        <s:param name="page" value="${currentIndex - 2}"/>
    </s:url>
    <s:url var="nextUrl" value="/admin/product/all">
        <up:urlParProdAdmin criteria="${criteriaAdmin}"/>
        <s:param name="page" value="${currentIndex + 1}"/>
    </s:url>

    <form:form action="${firstUrl}" modelAttribute="criteriaAdmin" method="get" cssClass="product-filter">
        <div class="collection-sort"><h1>Товари</h1></div>
        <div class="collection-sort">
            <form:label path="srt">Сортування:</form:label>
            <form:select path="srt" onchange="this.form.submit()">
                <form:options items="${srt.values}" itemLabel="value"/>
            </form:select>
            <form:label for="cur" path="cur">Валюта:</form:label>
            <form:select path="cur" id="cur" onchange="this.form.submit()">
                <form:option value="${null}">...Всі валюти...</form:option>
                <form:options items="${cur.values}"/>
            </form:select>
        </div>
        <div class="collection-sort">
            <form:label for="avail" path="avail">Доступність:</form:label>
            <form:select path="avail" id="avail" onchange="this.form.submit()">
                <form:option value="${null}">Всі товари</form:option>
                <form:option value="${true}">Доступні</form:option>
                <form:option value="${false}">Недоступні</form:option>
            </form:select>
            <form:label for="pr_vis" path="pr_vis">Показ ціни:</form:label>
            <form:select path="pr_vis" id="pr_vis" onchange="this.form.submit()">
                <form:option value="${null}">Всі товари</form:option>
                <form:option value="${true}">Так</form:option>
                <form:option value="${false}">Ні</form:option>
            </form:select>
            <form:label for="disc_pr" path="disc_pr">Знижка:</form:label>
            <form:select path="disc_pr" id="disc_pr" onchange="this.form.submit()">
                <form:option value="${null}">Всі товари</form:option>
                <form:option value="${true}">Зі знижкою</form:option>
                <form:option value="${false}">Без знижки</form:option>
            </form:select>
        </div>
        <div class="collection-sort">
            <form:label path="catId">Категорія:</form:label>
            <form:select path="catId" onchange="this.form.submit()">
                <form:option value="0" label="...Всі категорії..."/>
                <form:options items="${criteriaAdmin.categories}" itemValue="id" itemLabel="categoryName"/>
            </form:select>

            <form:label for="subId" path="subId">Категорії</form:label>
            <form:select multiple="single" path="subId" id="subId" onchange="this.form.submit()">
                <form:option value="0" label="...Вибрати..."/>
                <c:forEach var="itemGroup" items="${criteriaAdmin.categories}" varStatus="itemGroupIndex">
                    <optgroup label="${itemGroup.categoryName}">
                        <form:options items="${itemGroup.subcategoryList}" itemValue="id"
                                      itemLabel="subcategoryName"/>
                    </optgroup>
                </c:forEach>
            </form:select>

        </div>
        <div class="collection-sort">
            <form:label path="brandId">За брендом:</form:label>
            <form:select path="brandId" onchange="this.form.submit()">
                <form:option value="0">...Всі бренди...</form:option>
                <form:options items="${criteriaAdmin.brands}" itemValue="id" itemLabel="brandName"/>
            </form:select>
            <form:label path="search">Пошук:</form:label>
            <div>
                <form:input path="search" type="search"/>
                <form:button>Знайти</form:button>
            </div>
        </div>
        <div class="sort">
            <div><a class="button green-button" href="<s:url value="/admin/product/new"/>">Додати</a></div>
        </div>
    </form:form>
        <c:if test="${added eq true}">
            <div class="added">
                Товарну позицію з артикулом ${product.SKU} додано.
            </div>
        </c:if>
        <c:if test="${edited eq true}">
            <div class="edited">
                Товарну позицію з артикулом ${product.SKU} змінено.
            </div>
        </c:if>
        <c:if test="${removed eq true}">
            <div class="removed">
                Товарну позицію з артикулом ${product.SKU} видалено.
            </div>
        </c:if>
    <section class="datagrid">
        <table>
            <thead>
            <tr>
                <th>Арт.</th>
                <th>Назва</th>
                <th>Фото</th>
                <th>Доступ.</th>
                <th>Пок. ціну</th>
                <th>Вал.</th>
                <th>Ціна</th>
                <th>Зі знижкою</th>
                <th>Ціна грн.</th>
                <th>Продажі</th>
                <th>Підкатегорія</th>
                <th>Категорія</th>
                <th>Бренд</th>
                <th>Опції</th>
            </tr>
            </thead>
            <tfoot>
            <tr>
                <td colspan="14">
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
                            <s:url var="pageUrl" value="/admin/product/all">
                                <up:urlParProdAdmin criteria="${criteriaAdmin}"/>
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
                    <td>${temp.SKU}</td>
                    <td>${temp.productName}</td>
                    <td>
                        <c:choose>
                            <c:when test="${temp.imageAvailability == true}">
                                <span class="fa fa-check-square-o fa-2x" aria-hidden="true"
                                      style="color: green">&nbsp;</span>
                            </c:when>
                            <c:otherwise>
                                <span class="fa fa-times fa-2x" aria-hidden="true" style="color: red">&nbsp;</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${temp.availability == true}">
                                <span class="fa fa-check-square-o fa-2x" aria-hidden="true"
                                      style="color: green">&nbsp;</span>
                            </c:when>
                            <c:otherwise>
                                <span class="fa fa-times fa-2x" aria-hidden="true" style="color: red">&nbsp;</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${temp.priceVisibility == true}">
                            <span class="fa fa-check-square-o fa-2x" aria-hidden="true"
                                  style="color: green">&nbsp;</span>
                            </c:when>
                            <c:otherwise>
                                <span class="fa fa-times fa-2x" aria-hidden="true" style="color: red">&nbsp;</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${temp.currency}</td>
                    <td>
                        <fmt:formatNumber value="${temp.price}" type="number"
                                          minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${temp.discountPrice}" type="number"
                                          minFractionDigits="2"/>
                    </td>
                    <td>
                        <fmt:formatNumber value="${temp.priceUAH}" type="number"
                                          minFractionDigits="2"/>
                    </td>
                    <td>${temp.amountOfSales}</td>
                    <td>${temp.subcategory.subcategoryName}</td>
                    <td>${temp.subcategory.category.categoryName}</td>
                    <td>${temp.brand.brandName}</td>
                    <td>
                        <a href="<s:url value="/admin/product/edit=${temp.id}">
                                <up:urlParProdAdmin criteria="${criteriaAdmin}"/>
                                <s:param name="page" value="${currentIndex - 1}"/></s:url>">
                            <span class="fa fa-pencil-square-o fa-2x" aria-hidden="true"
                                style="color: darkorange">&nbsp;</span></a>
                        <a href="<s:url value="/admin/product/remove=${temp.id}">
                                <up:urlParProdAdmin criteria="${criteriaAdmin}"/>
                                <s:param name="page" value="${currentIndex - 1}"/></s:url>">
                            <span class="fa fa-trash-o fa-2x" aria-hidden="true" style="color: red"></span></a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </section>
</sec:authorize>
