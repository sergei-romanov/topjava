<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Meals</title>
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
<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
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
        <label><input type="text" name="calories" size="30" value="${meal.calories}"/></label></dl>
    <button type="submit">Save</button>
    <button type="reset" onclick="window.history.back()">Cancel</button>
</form>
</body>
</html>
