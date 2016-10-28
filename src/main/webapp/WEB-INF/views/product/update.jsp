<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="hasRole('ROLE_ADMIN')">
    <c:set var="context" value="${pageContext.request.contextPath}"/>
    <c:set var="imageUrl" value="/images/${product.SKU}/${product.SKU}_"/>
    <div class="product-filter">
        <div>
            <c:if test="${product.id != 0}"><h1>Редагувати товар</h1></c:if>
            <c:if test="${product.id == 0}"><h1>Додати товар</h1></c:if>
        </div>
        <div class="sort">
            <div><a class="button green-button" href="<s:url value="/admin/product/all"><s:param name="page" value="0"/></s:url>">Всі товари</a></div>
        </div>
    </div>
    <section class="background">
        <form:form method="post" modelAttribute="product" action="/admin/product/update" enctype="multipart/form-data">
            <div class="fieldset">
                <div>
                    <div>
                        <span>Фото</span>
                    </div>
                    <div>
                        <c:if test="${product.imageAvailability}">
                            <img property="image" data-sizes="auto"
                                 src="<s:url value="${context.concat(imageUrl).concat('400x400.jpg')}"/>"
                                 srcset="data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw=="
                                 data-srcset="
                             <s:url value="${context.concat(imageUrl).concat('400x400.jpg')}"/> 400w,
                         <s:url value="${context.concat(imageUrl).concat('300x300.jpg')}"/> 300w,
                         <s:url value="${context.concat(imageUrl).concat('500x500.jpg')}"/> 500w,
                         <s:url value="${context.concat(imageUrl).concat('600x600.jpg')}"/> 600w,
                         <s:url value="${context.concat(imageUrl).concat('700x700.jpg')}"/> 700w,
                         <s:url value="${context.concat(imageUrl).concat('800x800.jpg')}"/> 800w,
                         <s:url value="${context.concat(imageUrl).concat('900x900.jpg')}"/> 900w,
                         <s:url value="${context.concat(imageUrl).concat('1000x1000.jpg')}"/> 1000w,
                         <s:url value="${context.concat(imageUrl).concat('1100x1100.jpg')}"/> 1100w,
                         <s:url value="${context.concat(imageUrl).concat('1200x1200.jpg')}"/> 1200w,
                         <s:url value="${context.concat(imageUrl).concat('1300x1300.jpg')}"/> 1300w,
                         <s:url value="${context.concat(imageUrl).concat('1400x1400.jpg')}"/> 1400w,
                         <s:url value="${context.concat(imageUrl).concat('1500x1500.jpg')}"/> 1500w"
                                 alt="Product Image" class="lazyload">
                        </c:if>
                        <c:if test="${product.imageAvailability eq false}">
                            <img src="<s:url value="/static/images/no_photo_admin.svg"/>"
                                 alt="Image is not available">
                        </c:if>
                    </div>
                    <div>
                        <form:label for="image" path="image">Вибрати файл<br><span class="note">(Максимальний розмір - 1Мб)</span></form:label>
                        <form:input path="image" id="image" type="file"/>
                    </div>
                    <div>
                        <form:label for="imageAvailability" path="imageAvailability">Наявність фото<br><span class="note">(Якщо зняти, після підтвердження фото видалиться)</span></form:label>
                        <form:checkbox path="imageAvailability" id="imageAvailability"/>
                    </div>
                </div>
                <div>
                    <div>
                        <span>Номенклатура</span>
                    </div>
                    <div>
                        <form:hidden path="id"/>
                        <form:label for="SKU" path="SKU">Артикул</form:label>
                        <form:input path="SKU" id="SKU" placeholder="SKU"/>
                        <form:errors path="SKU" cssStyle="font-size: 12px; color: red"/>
                    </div>
                    <div>
                        <form:label for="productName" path="productName">Назва</form:label>
                        <form:textarea path="productName" id="productName" rows="6" cols="25"/>
                        <form:errors path="productName" cssStyle="font-size: 12px; color: red"/>
                    </div>
                    <div>
                        <form:label for="availability" path="availability">Показувати товар покупцям</form:label>
                        <form:checkbox path="availability" id="availability"/>
                    </div>
                </div>
                <div>
                    <div>
                        <span>Ціновий блок</span>
                    </div>
                    <div>
                        <form:label for="price" path="price">Ціна<br><span class="note">(У нижче визначеній валюті)</span></form:label>
                        <form:input path="price" id="price" placeholder="Price"/>
                    </div>
                    <div>
                        <form:label for="discountPrice" path="discountPrice">Знижкова ціна<br><span class="note">(У нижче визначеній валюті, діє, якщо більше нуля)</span></form:label>
                        <form:input path="discountPrice" id="discountPrice"/>
                    </div>
                    <div>
                        <form:label for="priceVisibility" path="priceVisibility">Показ ціни<br><span class="note">(Якщо не визначено, то показує "За запитом")</span></form:label>
                        <form:checkbox path="priceVisibility" id="priceVisibility"/>
                    </div>
                    <div>
                        <form:label for="currency" path="currency">Валюта</form:label>
                        <form:select path="currency" id="currency">
                            <form:options items="${currency.values}"/>
                        </form:select>
                    </div>
                </div>
                <div>
                    <div>
                        <span>Категоризація</span>
                    </div>
                    <div>
                        <form:label for="brand" path="brand.id">Бренд</form:label>
                        <form:select path="brand.id" id="brand">
                            <form:options items="${brands}" itemValue="id" itemLabel="brandName"/>
                        </form:select>
                    </div>
                    <div>
                        <form:label for="unit" path="unit.id">Одиниця</form:label>
                        <form:select path="unit.id" id="unit">
                            <form:options items="${units}" itemValue="id" itemLabel="unitName"/>
                        </form:select>
                    </div>
                    <div>
                        <form:label for="category" path="subcategory.id">Категорії</form:label>
                        <form:select multiple="single" path="subcategory.id" id="category">
                            <form:option value="0" label="...Вибрати..."/>
                            <c:forEach var="itemGroup" items="${categories}" varStatus="itemGroupIndex">
                                <optgroup label="${itemGroup.categoryName}">
                                    <form:options items="${itemGroup.subcategoryList}" itemValue="id"
                                                  itemLabel="subcategoryName"/>
                                </optgroup>
                            </c:forEach>
                        </form:select>
                        <form:errors path="subcategory.id"/>
                    </div>
                    <div>
                        <form:button name="Submit" type="submit">Підтвердити</form:button>
                    </div>
                </div>
            </div>
        </form:form>
    </section>
</sec:authorize>
