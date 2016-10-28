<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="title" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!DOCTYPE html>
<html lang="uk">
<head>
    <title>
        <title:getAsString name="title"/>
    </title>
    <meta name="copyright" content="&copy; 2016 Eugen Zubenko">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <link rel="apple-touch-icon" sizes="180x180" href="<s:url value="/static/favicons/apple-touch-icon.png"/>">
    <link rel="icon" type="image/png" href="<s:url value="/static/favicons/favicon-32x32.png"/>" sizes="32x32">
    <link rel="icon" type="image/png" href="<s:url value="/static/favicons/favicon-16x16.png"/>" sizes="16x16">
    <link rel="manifest" href="<s:url value="/static/favicons/manifest.json"/>">
    <link rel="mask-icon" href="<s:url value="/static/favicons/safari-pinned-tab.svg"/>" color="#5bbad5">
    <link rel="shortcut icon" href="<s:url value="/static/favicons/favicon.ico"/>">
    <meta name="apple-mobile-web-app-title" content="${contact.name}">
    <meta name="application-name" content="${contact.name}">
    <meta name="msapplication-config" content="<s:url value="/static/favicons/browserconfig.xml"/>">
    <meta name="theme-color" content="#ffffff">

    <link rel="stylesheet" type="text/css" href="<s:url value="/static/styles.css"/>">
    <link rel="stylesheet" type="text/css" href="<s:url value="/static/table.css"/>">
    <link rel="stylesheet" type="text/css" href="<s:url value="/static/datepicker/pikaday.css"/>">
    <link rel="stylesheet" type="text/css" href="<s:url value="/static/signs/css/font-awesome.min.css"/>">
    <link rel="stylesheet" type="text/css" href="<s:url value="/static/menu/css/sm-core-css.css"/>">
    <link rel="stylesheet" type="text/css" href="<s:url value="/static/menu/css/sm-blue.css"/>">

    <script type="text/javascript" src="<s:url value="/static/js/lazysizes.min.js"/>" async=""></script>

    <script type="text/javascript" src="<s:url value="/static/js/jquery-3.1.0.min.js"/>"></script>
    <script type="text/javascript" src="<s:url value="/static/menu/jquery.smartmenus.min.js"/>"></script>
    <script type="text/javascript">
        $(function () {
            $('#main-menu').smartmenus({
                subMenusSubOffsetX: 1,
                subMenusSubOffsetY: -8
            });
        });
    </script>
    <script src="<s:url value="/static/menu/html5shiv.min.js"/>"></script>
    <script src="<s:url value="/static/menu/respond.min.js"/>"></script>
</head>
<body class="site">
<header>
    <tiles:insertAttribute name="header"/>
</header>
<nav>
    <tiles:insertAttribute name="menu"/>
</nav>
<main>
    <tiles:insertAttribute name="body"/>
</main>
<footer>
    <tiles:insertAttribute name="footer"/>
</footer>
</body>
</html>