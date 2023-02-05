<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <style>
        table {
            border-collapse: collapse;
            border: 1px solid black;
        }

        th {
            border: 1px solid black;
            padding: 10px;
        }

        td {
            border: 1px solid black;
            padding: 10px;
        }

        .green {
            color: green;
        }

        .red {
            color: red;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h3><a href="users.jsp">Users</a></h3>
<h2>Meals</h2>
<h3><a href="meals?action=create">Add Meal</a></h3>
<br><br>
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <jsp:useBean id="meals" scope="request" type="java.util.List"/>
    <c:forEach items="${meals}" var="meal">
        <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr class="${meal.excess ? 'red' : 'green'}">
            <td>${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="meals?action=update&id=${meal.id}">update</a></td>
            <td><a href="meals?action=delete&id=${meal.id}">delete</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
