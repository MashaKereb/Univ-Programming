<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<form action="Main">
    <table border="2" >
        <tr>
            <th>

                <h2>Name</h2>
            </th>

            <th>
                <h3>Price</h3>

            </th>
            <th>
                <h3>Get it</h3>
            </th>
        </tr>

    <c:forEach items="${sessionScope.Menu.dishes}" var="dish" varStatus="theCount">
        <tr>
            <td><c:out value="${dish.name}"/></td>
            <td><c:out value="${dish.price}"/></td>
            <td>
                <input type="checkbox" name="${theCount.index}" value="${dish.id}">
            </td>
        </tr>
    </c:forEach>


    </table>
    <input type="text" name="table">
    <input type="submit" value="Confirm" name="command">
</form>
<c:choose>
    <c:when test="${sessionScope.User.admin == true}">
        <a href="Main?command=Bills"> administrator panel</a>
    </c:when>
</c:choose>
</body>
</html>
