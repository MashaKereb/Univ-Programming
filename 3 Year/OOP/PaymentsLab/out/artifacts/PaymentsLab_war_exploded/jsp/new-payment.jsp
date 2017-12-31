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
    <c:if test="${not empty message}">
        <div class="alert alert-info">
                ${message}
        </div>
    </c:if>
    <form action="/Main" method="post"  role="form" data-toggle="validator" >

        <h2>New Payment</h2>
        <div class="form-group col-xs-4">
            <input type="hidden" id="command" name="command" value="NewPayment">
            <input type="hidden" id="account_id" name="account_id" value="${account_id}">

            <label for="amount" class="control-label col-xs-4">Amount:</label>
            <input type="text" name="amount" id="amount" class="form-control" required="true" />

            <label for="type" class="control-label  col-xs-4">Type:</label>
            <select name="type"  id="type" class="form-control" required="true" data-live-search="true">

                <option value="Withdrawal">Withdrawal</option>
                <option value="Deposit">Deposit</option>
            </select>
            <br/>
            <button type="submit" class="btn btn-primary  btn-md">Accept</button>
        </div>
    </form>
</div>
</body>
</html>