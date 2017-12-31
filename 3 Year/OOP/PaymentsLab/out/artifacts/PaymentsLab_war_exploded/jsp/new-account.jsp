<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link rel="stylesheet" href="../css/bootstrap.min.css">
    <script src="../js/bootstrap.min.js"></script>
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
                <li><a href="/Main?command=AllPayments">Payments</a></li>
            </c:if>
        </ul>
    </div>
</header>
<div class="container">
    <form action="/Main" method="post"  role="form" data-toggle="validator" >
        <input type="hidden" id="action" name="command" value="NewAccount">

        <h2>Account</h2>
        <div class="form-group col-xs-4">
            <input type="hidden" id="user_id" name="user_id" value="${user_id}">
            <label for="number" class="control-label col-xs-4">Number:</label>
            <input type="text" name="number" id="number" class="form-control" required="true" />

            <label for="balance" class="control-label col-xs-4">Balance:</label>
            <input type="text" name="balance" id="balance" class="form-control" required="true" />

            <label for="status" class="control-label  col-xs-4">Type:</label>
            <select name="status"  id="status" class="form-control" required="true" data-live-search="true">

                <option value="Active">Active</option>
                <option value="Blocked">Blocked</option>
            </select>

            <br/>
            <button type="submit" class="btn btn-primary  btn-md">Accept</button>
        </div>
    </form>
</div>
</body>
</html>