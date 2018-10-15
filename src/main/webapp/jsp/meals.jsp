<%--
  Created by IntelliJ IDEA.
  User: plotnikov
  Date: 15.10.2018
  Time: 12:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h4>
    All Meals
</h4>
<table border="1" cellpadding="2" cellspacing="2" width="600">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <c:forEach var="elem" items="${meals}">
        <jsp:useBean id="elem" type="ru.javawebinar.topjava.model.MealWithExceed"/>
        <c:if test="${elem.exceed==true}">
            <tr bgcolor="#f08080">
                <td>
                   <javatime:format value="${elem.dateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </td>
                <td>${elem.description}</td>
                <td>${elem.calories}</td>
                <td>
                    <a href=meals?id=${elem.id}>
                        edit
                    </a>
                </td>
                <td>
                    <a href=meals?id=${elem.id}&del=1>
                        delete
                    </a>
                </td>
            </tr>
        </c:if>
        <c:if test="${elem.exceed==false}">
            <tr bgcolor="#90ee90">
                <td>
                    <javatime:format value="${elem.dateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </td>
                <td>${elem.description}</td>
                <td>${elem.calories}</td>
                <td>
                    <a href=meals?id=${elem.id}>
                        edit
                    </a>
                </td>
                <td>
                    <a href=meals?id=${elem.id}&del=1>
                        delete
                    </a>
                </td>
            </tr>
        </c:if>
    </c:forEach>
</table>
<br>
<br>
<a href="meals?id=-1">
    Add new meal
</a>
</body>
</html>
