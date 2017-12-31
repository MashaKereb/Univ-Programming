<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link rel="stylesheet" href="../css/bootstrap.min.css">
    <script src="../js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">
    <form action="/user" method="post"  role="form" data-toggle="validator" >
        <c:if test ="${empty action}">
            <c:set var="action" value="add"/>
        </c:if>
        <input type="hidden" id="action" name="action" value="${action}">
        <input type="hidden" id="idUser" name="idUser" value="${user.id}">
        <h2>User</h2>
        <div class="form-group col-xs-4">
            <label for="name" class="control-label col-xs-4">Name:</label>
            <input type="text" name="name" id="name" class="form-control" value="${user.name}" required="true" />

            <label for="email" class="control-label col-xs-4">Email:</label>
            <input type="email" name="email" id="email" class="form-control" value="${user.email}" required="true" />

            <label for="role" class="control-label  col-xs-4">Role:</label>
            <select name="role" value="${user.role}" id="role" class="form-control" required="true" data-live-search="true">

                <option value="Manager" ${user.role == "Manager" ? 'selected="selected"' : ''}>Manager</option>
                <option value="User" ${user.role == "User" ? 'selected="selected"' : ''}>User</option>
                <option value="PremiumUser" ${user.role == "PremiumUser" ? 'selected="selected"' : ''}>PremiumUser</option>
                <option value="Admin" ${user.role == "Admin" ? 'selected="selected"' : ''}>Admin</option>
                <option value="Moderator" ${user.role == "Moderator" ? 'selected="selected"' : ''}>Moderator</option>
            </select>

            <label for="status" class="control-label col-xs-4">Status:</label>
            <select name="status" id="status" value="${user.status}" class="form-control" required="true" data-live-search="true">

                <option value="New" ${user.status == "New" ? 'selected="selected"' : ''}>New</option>
                <option value="Active" ${user.status == "Active" ? 'selected="selected"' : ''}>Active</option>
                <option value="Blocked" ${user.status == "Blocked" ? 'selected="selected"' : ''}>Blocked</option>
                <option value="Staff" ${user.status == "Staff" ? 'selected="selected"' : ''}>Staff</option>
            </select>

            <label for="group" class="control-label col-xs-4">Group:</label>
            <select name="idGroup" id="group" class="form-control" required="true" data-live-search="true">
                <option value="-1" selected="selected"}>No group</option>
                <c:forEach var="group" items="${groupList}">
                    <option value="${group.id}" ${group.id == user.group.id ? 'selected="selected"' : ''}>${group.name}</option>
                </c:forEach>
            </select>

            <br></br>
            <button type="submit" class="btn btn-primary  btn-md">Accept</button>
        </div>
    </form>
</div>
</body>
</html>