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
    <title>Přihlašovací obrazovka</title>
</head>
<body>
<div id="mainWrapper">
    <div class="login-container">
        <div class="login-card">
            <div class="login-form">
                <c:url var="loginUrl" value="/changePassword"/>
                <form action="${loginUrl}" method="post" class="form-horizontal">
                    <div class="input-group input-sm">
                        <label class="input-group-addon" for="password"><i class="fa fa-lock"></i></label>
                        <input type="password" class="form-control" id="password" name="password" placeholder="Zadejte původní heslo" required>
                    </div>
                    <div class="input-group input-sm">
                        <label class="input-group-addon" for="password1"><i class="fa fa-lock"></i></label>
                        <input type="password" class="form-control" id="password1" name="password1" placeholder="Zadejte nové heslo" required>
                    </div>
                    <div class="input-group input-sm">
                        <label class="input-group-addon" for="password2"><i class="fa fa-lock"></i></label>
                        <input type="password" class="form-control" id="password2" name="password2" placeholder="Zadejte znovu nové heslo" required>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <div class="form-actions">
                        <input type="submit"
                               class="btn btn-block btn-primary btn-default" value="Log in">
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
