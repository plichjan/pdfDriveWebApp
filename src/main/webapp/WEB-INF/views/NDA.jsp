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
    <title>NDA</title>
</head>
<body>
<div id="mainWrapper">
    <div class="login-container">

    <h1>Dohoda o mlčenlivosti, ochraně informací a zákazu jejich zneužití</h1>

    <h2>Článek I.</h2>
    <ol>
        <li>Účelem této Dohody je ochrana důvěrných informací Společenství pro dům Rezlerova 285 až 288, IČ 24683060 (dále jen „SVJ“), se kterými se uživatel seznámí na těch webových stránkách.</li>
        <li>Předmětem této Dohody je bližší vymezení důvěrných informaci a převzetí závazku uživatele zachovat o těchto důvěrných informacích mlčenlivost a nesdělit je ani neumožnit k nim přístup třetím osobám, nebo je nevyužít ve svůj prospěch nebo ve prospěch třetích osob, není-li v této Dohodě stanoveno jinak.</li>
        <li>Důvěrnými informacemi se pro účely této Dohody rozumí jakékoli a všechny skutečnosti, které se uživatel na těchto webových stránkách dozví (dále jen „Důvěrné informace“).</li>
    </ol>

    <h2>Článek II.</h2>
    <ol>
        <li>Uživatel se zavazuje, že veškeré skutečnosti spadající mezi Důvěrné informace nebude dále rozšiřovat nebo reprodukovat a nezpřístupní je třetí straně. Současně se zavazuje, že zabezpečí, aby převzaté dokumenty a případné analýzy obsahující Důvěrné informace byly řádně evidovány. Uživatel se dále zavazuje, že Důvěrné informace nepoužije v rozporu s jejich účelem ani účelem jejich poskytnutí pro své potřeby nebo ve prospěch třetích osob.</li>
        <li>Uživatel přijme účinná opatření pro zamezení úniku informací.</li>
        <li>Povinnost plnit ustanovení této Dohody se nevztahuje na chráněné informace, které:
            <ol type="a">
                <li>mohou být zveřejněny bez porušení této smlouvy;</li>
                <li>byly písemným souhlasem provozovatele stránek uvolněny od těchto omezení;</li>
                <li>jsou veřejně dostupné nebo byly zveřejněny jinak, než porušením povinnosti jiného uživatele;</li>
                <li>uživatel je zná zcela prokazatelně dříve, než je získal na těchto webových stránkách;</li>
                <li>jsou vyžádány soudem, státním zastupitelstvím nebo věcně příslušným správním orgánem na základě zákona a jsou použity pouze k tomuto účelu.</li>
            </ol>
        </li>
        <li>Veškeré informace poskytnuté na těchto webových stránkách jsou určeny pro interní potřebu SVJ a uživatel, jakožto člen SVJ, si je může vyžádat k nahlédnutí u výboru SVJ.</li>
    </ol>

    <h2>Článek III.</h2>
    <ol>
        <li>Způsobí-li uživatel škodu SVJ porušením této smlouvy, odpovídá za ni dle obecných právních předpisů.</li>
    </ol>

    <p style="text-align: center; margin-top: 2em">Kliknutím na odkaz „Souhlasím s dohodou“ se uživatel zavazuje k jejímu dodržování.</p>
    <p style="text-align: center"><a href="<c:url value="/nda?status=ok"/>">SOUHLASÍM S DOHODOU</a></p>

    </div>
</div>
</body>
</html>
