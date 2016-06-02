<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>List of all PDF</title>
</head>
<body>
<%--@elvariable id="dir" type="com.amazonaws.services.s3.model.ObjectListing"--%>
<c:set var="prefix" value=""/>
<c:url var="url" value="/dir/"/>
<a href="${url}">.</a>
<c:forTokens var="s" items="${dir.prefix}" delims="/">
    <c:set var="prefix" value="${prefix}${s}/"/>
    <c:url var="url" value="/dir/${prefix}"/>
    / <a href="${url}">${s}</a>
</c:forTokens>
<ul>
    <c:forEach var="prefix" items="${dir.commonPrefixes}">
        <c:url var="url" value="/dir/${prefix}"/>
    <li><a href="${url}">${fn:replace(prefix, dir.prefix, '')}</a></li>
    </c:forEach>
    <c:forEach var="item" items="${dir.objectSummaries}">
        <c:url var="url" value="/download/${item.key}"/>
    <li><a href="${url}">${fn:replace(item.key, dir.prefix, '')}</a></li>
    </c:forEach>
</ul>

</body>
</html>
