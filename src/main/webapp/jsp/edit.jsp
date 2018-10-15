<jsp:useBean id="id" scope="request" type="java.lang.Integer"/>
<%--
  Created by IntelliJ IDEA.
  User: plotnikov
  Date: 15.10.2018
  Time: 14:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Edit meal</title>
</head>
<body>
<c:if test="${id>=0}">
    <jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>

    <form action="meals" method="post">
        <p><input type="hidden" size="40" name="id" value=${id}></p>

        <p><b>Date:</b></p>
        <p><input type="datetime-local" size="40" name="date" value="${meal.dateTime}"></p>

        <p><b>Description:</b></p>
        <p><input type="text" size="40" name="desc" value="${meal.description}"></p>

        <p><b>Calories:</b></p>
        <p><input type="text" size="40" name="cal" value="${meal.calories}"></p>

        <p><input type="submit" value="Add"></p>
        <p><input type="button" value="Cancel" onclick="history.back()"></p>
    </form>
</c:if>
<c:if test="${id<0}">
    <form action="meals" method="post">
        <p><input type="hidden" size="40" name="id" value=${id}></p>

        <p><b>Date:</b></p>
        <p><input type="datetime-local" size="40" name="date"></p>

        <p><b>Description:</b></p>
        <p><input type="text" size="40" name="desc"></p>

        <p><b>Calories:</b></p>
        <p><input type="text" size="40" name="cal"></p>

        <p><input type="submit" value="Add"></p>
        <p><input type="button" value="Cancel" onclick="history.back()"></p>
    </form>
</c:if>
</body>
</html>
