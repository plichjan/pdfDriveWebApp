<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
  <title>pdfDrive SVJ Rezlerova 285 až 288</title>
</head>
<body>
<%--@elvariable id="_csrf" type="org.springframework.security.web.csrf.DefaultCsrfToken"--%>
<c:url value="/logout" var="logoutUrl" />
<form action="${logoutUrl}" method="post" id="logoutForm">
    <input type="hidden" name="${_csrf.parameterName}"
           value="${_csrf.token}" />
</form>
<script>
    function formSubmit() {
        document.getElementById("logoutForm").submit();
    }
</script>

<h2>pdfDrive</h2>
    <ul>
        <sec:authorize access="isAuthenticated()">
            <li><a href="<c:url value="/changePassword"/>">Změna hesla</a></li>
            <li><a href="javascript:formSubmit()">Odhlásit</a></li>
        </sec:authorize>
        <sec:authorize access="hasRole('ADMIN')">
            <li><a href="<c:url value="/setPassword"/>">Nastavit někomu heslo</a></li>
            <li><a href="<c:url value="/addUser"/>">Přidat uživatele</a></li>
        </sec:authorize>
    </ul>
</body>
</html>
