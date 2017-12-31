<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <link rel="stylesheet" href="../css/bootstrap.min.css"/>
    <script src="../js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="../css/custom.css"/>
    <link rel="stylesheet" href="../css/header.css"/>
</head>

<body>
<header>
    <div class="nav">
        <ul>
            <c:if test="${sessionScope.user.type == 'Admin'}">
                <li><a href="/Main?command=AllUsers">Users</a></li>
            </c:if>
            <li><a href="/Main?command=AllAccounts"> Accounts</a></li>
            <c:if test="${sessionScope.user.type == 'Admin'}">
                <li><a class="active" href="/Main?command=AllPayments">Payments</a></li>
            </c:if>
        </ul>
    </div>
</header>
<div class="container">


    <c:if test="${not empty message}">
        <div class="alert alert-success">
                ${message}
        </div>
    </c:if>


    <form action="/user" method="post" id="userForm" role="form">
        <input type="hidden" id="idUser" name="idUser">
        <input type="hidden" id="action" name="action">
    </form>
    <c:choose>
        <c:when test="${not empty paymentList}">
            <table class="table table-hover">
                <thead class="thead-inverse">
                <tr>
                    <td>#</td>
                    <td>Account</td>
                    <td>Amount</td>
                    <td>Type</td>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="payment" items="${paymentList}" varStatus="loop">
                    <c:set var="classSucess" value=""/>
                    <c:if test="${payment_id == payment.id}">
                        <c:set var="classSucess" value="info"/>
                    </c:if>
                    <tr class="${classSucess}">
                        <td>${payment.id}</td>
                        <td>${payment.account.number}</td>
                        <td>${payment.amount}</td>
                        <td>${payment.type}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <br>
            <div class="alert alert-info">
                No payments found
            </div>
        </c:otherwise>
    </c:choose>

    <c:if test="${sessionScope.user.type == 'User' and not empty account_id}">
        <form action="/Main" method="get" role="form">
            <input type="text" name="account_id" value="${account_id}" hidden="true"/>
            <input type="text" name="command" value="PaymentCreation" hidden="true"/>
            <button type="submit" class="btn btn-primary  btn-md">New payment</button>
        </form>
    </c:if>

</div>
</body>
</html>