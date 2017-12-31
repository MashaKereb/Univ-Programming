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
            <li><a class="active" href="/Main?command=AllAccounts"> Accounts</a></li>
            <c:if test="${sessionScope.user.type == 'Admin'}">
                <li><a href="/Main?command=AllPayments">Payments</a></li>
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

    <c:choose>
        <c:when test="${not empty accountList}">
            <table class="table table-hover">
                <thead class="thead-inverse">
                <tr>
                    <td>#</td>
                    <td>Number</td>
                    <td>Balance</td>
                    <td>Status</td>
                    <c:if test="${sessionScope.user.type == 'Admin'}">
                        <td>User id</td>
                        <td/>
                        <td/>
                    </c:if>
                    <td/>

                </tr>
                </thead>
                <tbody>
                <c:forEach var="account" items="${accountList}" varStatus="loop">
                    <c:set var="classSucess" value=""/>
                    <c:if test="${account_id == account.id}">
                        <c:set var="classSucess" value="info"/>
                    </c:if>
                    <tr class="${classSucess}">
                        <td>${account.id}</td>
                        <td>${account.number}</td>
                        <td>${account.balance}</td>
                        <td>${account.status}</td>
                        <c:if test="${sessionScope.user.type == 'Admin'}">
                            <td>${account.user.id}</td>
                            <td><a href="\Main?command=RemoveAccount&account_id=${account.id}" id="remove">
                                <span class="glyphicon glyphicon-trash"/>
                            </a></td>
                        </c:if>
                        <td><a href="\Main?command=AllPayments&account_id=${account.id}" id="payments">
                            <span class="glyphicon glyphicon-folder-open"/>
                        </a></td>
                        <c:if test="${sessionScope.user.type == 'Admin'}">
                            <td><a href="\Main?command=BlockAccount&account_id=${account.id}" id="block">
                                <span class="glyphicon glyphicon-remove"/>
                            </a>
                                <a href="\Main?command=UnblockAccount&account_id=${account.id}" id="unblock">
                                    <span class="glyphicon glyphicon-ok"/>
                                </a></td>
                        </c:if>

                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <br>
            <div class="alert alert-info">
                No accounts found
            </div>
        </c:otherwise>
    </c:choose>

    <c:if test="${sessionScope.user.type == 'Admin' and not empty user_id}">
        <form action="/Main" method="get" role="form">
            <br/>
            <input type="text" name="user_id" value="${user_id}" hidden="true"/>
            <input type="text" name="command" value="AccountCreation" hidden="true"/>
            <button type="submit" class="btn btn-primary  btn-md">New account</button>
        </form>
    </c:if>

</div>
</body>
</html>