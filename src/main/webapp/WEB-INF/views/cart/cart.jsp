<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="context" value="${pageContext.request.contextPath}"/>
<sec:authorize access="hasRole('ROLE_USER') or isAnonymous()">
    <div class="product-filter">
        <div><h1>Замовлення</h1></div>
        <div class="sort">
            <c:choose>
                <c:when test="${cart.allowSale}">
                    <div>
                        <a class="button green-button" href="<s:url value="/customer/order/new"/>">Далі</a>
                    </div>
                </c:when>
                <c:when test="${!cart.allowSale}">
                    <div>
                        <a class="button" href="<s:url value="/products"><s:param name="page" value="0"/></s:url>">На головну</a>
                    </div>
                </c:when>
            </c:choose>
        </div>
    </div>
    <section class="background">
        <c:if test="${!cart.allowSale}">
            <article>
                <span style="color: red">Мінімальна ціна замовлення становить&nbsp;${cart.getMinSumOfOrder()}&nbsp;грн.
                    Для здійснення замовлення необхідно додати товарів на&nbsp;${cart.getRestToMinSum()}&nbsp;грн.
                </span>
            </article>
        </c:if>
        <c:forEach var="order" items="${cart.entryList}">
            <article class="cart-content">
                <div>
                    <c:if test="${order.imageAvailability}">
                        <c:set var="imageUrl" value="/images/${order.SKU}/${order.SKU}_"/>
                        <img class="lazyload" data-sizes="auto"
                             src="data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw=="
                             data-src="<s:url value="${context.concat(imageUrl).concat('400x400.jpg')}"/>"
                             data-srcset="
                         <s:url value="${context.concat(imageUrl).concat('300x300.jpg')}"/> 300w,
                         <s:url value="${context.concat(imageUrl).concat('400x400.jpg')}"/> 400w,
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
                             alt="Product Image"/>
                    </c:if>
                    <c:if test="${order.imageAvailability eq false}">
                        <img src="<s:url value="/static/images/no_photo.svg"/>" alt="Image is not available"/>
                    </c:if>
                </div>
                <div><span>${order.productName}</span></div>
                <div><span>${order.stringPriceUAH}&nbsp;грн&nbsp;/&nbsp;${order.unit}</span>
                </div>
                <div><span>${order.quantity}&nbsp;${order.unit}</span></div>
                <div><span>${order.stringSubtotal}&nbsp;грн</span>
                </div>
                <div>
                    <a href="<s:url value="/cart/remove=${order.id}"/>"><span class="fa fa-minus-square-o fa-2x"
                                                                            style="color: #EC7063"
                                                                            aria-hidden="true"></span></a>
                    <span>&nbsp;&nbsp;</span>
                    <a href="<s:url value="/cart/add=${order.id}"/>"><span class="fa fa-plus-square-o fa-2x"
                                                                         style="color: #58D68D"
                                                                         aria-hidden="true"></span></a>
                </div>
            </article>
        </c:forEach>
        <article class="cart-footer">
            <div></div>
            <div><h6>Разом:&nbsp;&nbsp;${cart.stringTotalPrice}&nbsp;грн</h6></div>
            <div><a class="button grey-button" href="<s:url value="/cart/clear"/>">Очистити</a></div>
        </article>
    </section>
</sec:authorize>
