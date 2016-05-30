<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>List of all PDF</title>
</head>
<body>
<ul>
    <%--@elvariable id="pdfs" type="org.springframework.core.io.Resource[]"--%>
    <c:forEach var="pdf" items="${pdfs}">
        <c:url var="url" value="/download/${pdf.filename}"/>
    <li><a href="${url}">${pdf.filename}</a></li>
    </c:forEach>
</ul>

</body>
</html>
