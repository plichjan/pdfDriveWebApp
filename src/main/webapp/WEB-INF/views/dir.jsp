<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>List of all PDF</title>
</head>
<body>
<%--@elvariable id="root" type="java.lang.String"--%>
<%--@elvariable id="parent" type="java.lang.String"--%>
<%--@elvariable id="dir" type="java.util.List<java.io.File>"--%>
<c:set var="prefix" value=""/>
<c:url var="url" value="/dir/"/>
<a href="${url}">.</a>
<c:forTokens var="s" items="${parent}" delims="/">
    <c:set var="prefix" value="${prefix}${s}/"/>
    <c:url var="url" value="/dir/${prefix}"/>
    / <a href="${url}">${s}</a>
</c:forTokens>
<ul>
    <c:forEach var="file" items="${dir}">
        <c:if test="${file.directory}">
            <c:url var="url" value="/dir/${parent}${file.name}/"/>
        <li><a href="${url}">${file.name}/</a></li>
        </c:if>
    </c:forEach>
    <c:forEach var="file" items="${dir}">
        <c:if test="${file.file}">
            <c:url var="url" value="/download/${parent}${file.name}"/>
        <li><a href="${url}">${file.name}</a></li>
        </c:if>
    </c:forEach>
</ul>

</body>
</html>
