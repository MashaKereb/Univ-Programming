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
                <li><a class="active" href="/Main?command=AllUsers">Users</a></li>
            </c:if>
            <li><a href="/Main?command=AllAccounts"> Accounts</a></li>
            <c:if test="${sessionScope.user.type == 'Admin'}">
                <li><a href="/Main?command=AllPayments">Payments</a></li>
            </c:if>
        </ul>
    </div>
</header>
<div class="container">

    <div><h2>Users</h2></div>

    <c:if test="${not empty message}">
        <div class="alert alert-success">
                ${message}
        </div>
    </c:if>

    <!--Users List-->
    <form action="/user" method="post" id="userForm" role="form">
        <input type="hidden" id="idUser" name="idUser">
        <input type="hidden" id="action" name="action">
    </form>
    <c:choose>
        <c:when test="${not empty userList}">
            <table class="table table-hover">
                <thead class="thead-inverse">
                <tr>
                    <td>#</td>
                    <td>Name</td>
                    <td>Surname</td>
                    <td>E-mail</td>
                    <td>Type</td>
                    <td>Accounts</td>
                    <td/>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="user" items="${userList}" varStatus="loop">
                    <c:set var="classSuccess" value=""/>
                    <c:if test="${user_id == user.id}">
                        <c:set var="classSuccess" value="info"/>
                    </c:if>
                    <tr class="${classSuccess}">
                        <td>${user.id}</td>
                        <td>${user.name}</td>
                        <td>${user.surname}</td>
                        <td>${user.email}</td>
                        <td>${user.type}</td>
                        <td><a href="/Main?command=AllAccounts&user_id=${user.id}" id="accounts">
                            <span class="glyphicon glyphicon-folder-open"/>
                        </a></td>
                        <td><a href="/Main?command=RemoveUser&user_id=${user.id}" id="remove">
                            <span class="glyphicon glyphicon-trash"/>
                        </a></td>

                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <br>
            <div class="alert alert-info">
                No people found matching your search criteria
            </div>
        </c:otherwise>
    </c:choose>

    <form action="/jsp/new-user.jsp" method="get" role="form">
        <button type="submit" class="btn btn-primary  btn-md">New user</button>
    </form>

</div>
</body>
</html>