<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .exceeded {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <h2>Meals</h2>
    <a href="meals?action=create">Add Meal</a>
    <hr/>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
            <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
<form action="meals?action=allfilter" method="post">
    <dl>
        <dt>Date1:</dt>
        <dd><input type="date" name="date1" required></dd>
    </dl>
    <dl>
        <dt>Date2:</dt>
        <dd><input type="date" name="date2" required></dd>
    </dl>
    <input type="submit" value="Отобрать по датам"></p>
</form>
<form action="meals?action=allfilter" method="post">
    <dl>
        <dt>Time1:</dt>
        <dd><input type="time" name="time1" required></dd>
    </dl>
    <dl>
        <dt>Time2:</dt>
        <dd><input type="time" name="time2" required></dd>
    </dl>
    <input type="submit" value="Отобрать"></p>
</form>
<form action="meals?action=all" method="get">
    <input type="submit" value="Сбросить отборы"></p>
</form>
</body>
</html>