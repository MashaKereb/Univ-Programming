
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title></title>
</head>
<body>
<table border="2" >
    <tr>
        <th>

            <h2>Table</h2>
        </th>
        <th>
            <h2>Price</h2>
        </th>
        <th>
            <h2>
                Dishes
            </h2>
        </th>
        <th>
            <h2>Confirm</h2>
        </th>
    </tr>

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
                <form>
                    <button name="billID" value="${bill.id}" class="button" >confirm</button>
                    <input name="command" type="hidden" id="hidden" value="Bills">
                </form>

            </td>
        </tr>
    </c:forEach>


</table>
<a href="Main?command=Menu">Menu</a>

</body>
</html>
