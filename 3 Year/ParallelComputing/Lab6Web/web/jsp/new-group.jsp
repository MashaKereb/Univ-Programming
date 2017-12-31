<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link rel="stylesheet" href="../css/bootstrap.min.css">
    <script src="../js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <form action="/group" method="post"  role="form" data-toggle="validator" >
        <c:if test ="${empty action}">
            <c:set var="action" value="add"/>
        </c:if>
        <input type="hidden" id="action" name="action" value="${action}">
        <input type="hidden" id="idGroup" name="idGroup" value="${group.id}">
        <h2>Groups</h2>
        <div class="form-group col-xs-4">
            <label for="name" class="control-label col-xs-4">Name:</label>
            <input type="text" name="name" id="name" class="form-control" value="${group.name}" required="true" />

            <label for="description" class="control-label col-xs-4">Description:</label>
            <input type="text" name="description" id="description" class="form-control" value="${group.description}" required="true"/>

            <br></br>
            <button type="submit" class="btn btn-primary  btn-md">Accept</button>
        </div>
    </form>
</div>
</body>
</html>