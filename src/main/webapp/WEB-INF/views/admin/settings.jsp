<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="hasRole('ROLE_ADMIN')">
    <div class="product-filter">
        <div><h1>Налаштування</h1></div>
        <div class="sort">
            <div><a class="button green-button" href="<s:url value="/admin/order/all"><s:param name="page" value="0"/></s:url>">На головну</a></div>
        </div>
    </div>
    <section class="background">
        <div class="fieldset">
            <div>
                <form:form method="post" action="/admin/settings/update" modelAttribute="settings"
                           cssClass="flex-outer">
                    <div>
                        <span>Мінімальна сумма замовлення</span>
                    </div>
                    <div>
                        <form:label for="minSum" path="minSumOfOrder">Сума (грн)</form:label>
                        <form:input path="minSumOfOrder" id="minSum"/>
                    </div>
                    <div>
                        <form:button name="submit" value="submit">Підтвердити</form:button>
                    </div>
                </form:form>
            </div>
            <div style="background: #ffcccc; justify-content: space-between">
                <div>
                    <h4>Пакетне додавання фото</h4>
                    <p style="font-size: 12px">Для пакетного додавання фото через данну опцію потрібно у корневій
                        директорії сервера-контейнера
                        Tomcat знайти або створити папку upload, до якої помістити фото у форматі JPEG, при чому
                        необхідно, щоб назва файлу являла собою артикул товарної позиції, наприклад, файл 5678.jpg
                        буде присвоєний до товарної позиції з артикулом 5678. Товарна позиція вже має існувати в
                        базі.</p>
                    <p style="font-size: 12px">Процес конвертації складається з нарізання фото на розміри від 300x300px
                        до 1500x1500px з кроком
                        100px за алгоритмом ресемплінгу LANCZOS, тож процес повільний, якість висока. Фактор компресії
                        JPEG - 0.9.</p>
                    <p style="font-size: 12px">Якщо фото має інше співвідношення сторін ніж 1x1, то буде проведене
                        обрізання (crop) по центру,
                        тож щоб уникнути втрати важливих елементів на фото вкрай бажано подавати на обробку зображення
                        вже з співвідношенням сторін 1x1 та задля досягнення максимальної якості в розділовій здатності
                        не нижче 1500 по меншій стороні.</p>
                </div>
                <div style="justify-content: flex-end">
                    <a class="button grey-button" href="<s:url value="/admin/product/addPhotosInBatch"/>">Запуск</a>
                </div>
            </div>
            <div style="background: #ffcccc; justify-content: space-between">
                <div>
                    <h4>Створення бази з документу Excel</h4>
                    <p style="font-size: 12px">Для додавання або внесення змін до товарних позицій через данну опцію
                        потрібно у корневій
                        директорії сервера-контейнера Tomcat знайти або створити папку excel до якої помістити Excel
                        файл santex_db.xlsx (що базується на XML). </p>
                </div>
                <div style="justify-content: flex-end">
                    <a class="button grey-button" href="<s:url value="/admin/product/db_creation"/>">Запуск</a>
                </div>
            </div>
            <div style="background: #ffcccc; justify-content: space-between">
                <div>
                    <h4>Cache preheat</h4>
                </div>
                <div style="justify-content: flex-end">
                    <a class="button grey-button" href="<s:url value="/admin/product/cachePreheat"/>">Запуск</a>
                </div>
            </div>
        </div>
    </section>
</sec:authorize>

