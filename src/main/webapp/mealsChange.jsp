<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<html lang="ru">
<head>
    <c:if test="${meal.id == null}"><title>Add Meal</title></c:if>
    <c:if test="${meal.id != null}"><title> Edit meal</title></c:if>
    <style>
        dl {
            padding: 1px;
        }

        dt {
            display: inline-block;
            width: 150px;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Add meal</h2>
<form method="post" action="meals">
    <input type="hidden" name="id" value="${meal.id}">
    <dl>
        <dt>DateTime:</dt>
        <label><input type="datetime-local" name="dateTime" size="20" value="${meal.dateTime}"/></label></dl>
    <dl>
        <dt>Description:</dt>
        <label><input type="text" name="descriptions" size="50" value="${meal.description}"/></label></dl>
    <dl>
        <dt>Calories:</dt>
        <label><input type="number" name="calories" size="30" value="${meal.calories}"/></label></dl>
    <button type="submit">Save</button>
    <button type="reset" onclick="window.history.back()">Cancel</button>
</form>
</body>
</html>
