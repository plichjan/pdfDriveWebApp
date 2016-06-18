<%--@elvariable id="_csrf" type="org.springframework.security.web.csrf.DefaultCsrfToken"--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7"
          crossorigin="anonymous">
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet" integrity="sha384-T8Gy5hrqNKT+hzMclPo118YTQO6cYprQmhrYwIiQ/3axmI1hQomh7Ud2hPOy8SP1"
          crossorigin="anonymous">
    <link href="<c:url value='/static/css/app.css' />" rel="stylesheet"/>
    <title>Nastavení hesla</title>
</head>
<body>
<div id="mainWrapper">
    <div class="login-container">
        <div class="login-card">
            <div class="login-form">
                <c:url var="setPassword" value="/setPassword"/>
                <form action="${setPassword}" method="post" class="form-horizontal">
                    <div class="input-group input-sm">
                        <label class="input-group-addon" for="username"><i class="fa fa-user"></i></label>
                        <input type="text" class="form-control" id="username" name="username" placeholder="Zadejte uživatelské jméno tj. email" required>
                    </div>
                    <div class="input-group input-sm">
                        <label class="input-group-addon" for="password"><i class="fa fa-lock"></i></label>
                        <input type="password" class="form-control" id="password" name="password" placeholder="Zadejte heslo" required>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <div class="form-actions">
                        <input type="submit"
                               class="btn btn-block btn-primary btn-default" value="Nastavit heslo">
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
