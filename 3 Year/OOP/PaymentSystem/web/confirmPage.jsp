<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>confrim page</title>
</head>
<body>
<c:forEach items="${requestScope.Bills.bills}" var="bill" varStatus="theCount">
    <tr>
        <td><c:out value="${bill.table}"/></td>
        <td><c:out value="${bill.price}"/></td>
        <td>
            <c:forEach items="${bill.dishes}" var="dish">
                <c:out value="${dish.name}"/> <br>
            </c:forEach>
        </td>
        <td>
            <form action="Main">
                <button name="billID" value="${bill.id}" class="button" >
                    pay
                </button>
                <input name="command" type="hidden" id="hidden" value="Pay">
            </form>
        </td>
    </tr>
</c:forEach>
<a href="Main?command=Menu">Menu</a>
</body>
</html>
