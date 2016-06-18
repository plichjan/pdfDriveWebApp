<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <ul>
        <li><a href="<c:url value="/changePassword"/>">Změna hesla</a></li>
        <li><a href="javascript:formSubmit()">Odhlásit</a></li>
    </ul>
</body>
</html>
