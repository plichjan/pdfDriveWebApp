<%--@elvariable id="_csrf" type="org.springframework.security.web.csrf.DefaultCsrfToken"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Přihlašovací obrazovka</title>
</head>
<body>
<c:url value="/login" var="loginUrl"/>
<form action="${loginUrl}" method="post">
    <c:if test="${param.error != null}">
        <p>
            Špatné přihlašovací jméno nebo heslo.
        </p>
    </c:if>
    <c:if test="${param.logout != null}">
        <p>
            Byli jste odhlášeni.
        </p>
    </c:if>
    <p>
        <label for="username">Přihlašovací jméno</label>
        <input type="text" id="username" name="username"/>
    </p>
    <p>
        <label for="password">Heslo</label>
        <input type="password" id="password" name="password"/>
    </p>
    <input type="hidden"
           name="${_csrf.parameterName}"
           value="${_csrf.token}"/>
    <button type="submit" class="btn">Přihlásit se</button>
</form>
</body>
</html>
