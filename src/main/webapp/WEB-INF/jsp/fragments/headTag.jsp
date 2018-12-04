<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title><spring:message code="app.title"/></title>

    <%--https://stackoverflow.com/questions/4764405/how-to-use-relative-paths-without-including-the-context-root-name--%>
    <base href="/topjava/"/>

   <%-- after <base/>--%>
    <link rel="stylesheet" href="resources/css/style.css">
</head>