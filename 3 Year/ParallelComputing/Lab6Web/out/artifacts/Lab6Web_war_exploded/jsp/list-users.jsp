<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <link rel="stylesheet" href="../css/bootstrap.min.css"/>
    <script src="../js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="../css/custom.css"/>
</head>

<body>
<div class="container">
    <div><div><h2>Users</h2></div>  <div class="display-right"><a href="/group" ><h2>Groups</h2></a></div></div>

    <!--Search Form -->
    <form action="/user" method="get" id="seachUserForm" role="form">
        <input type="hidden" id="searchAction" name="searchAction" value="searchByName">
        <div class="form-group col-xs-5">
            <input type="text" name="userName" id="userName" value="${searchVal}" class="form-control" required="true" placeholder="Type the Name of the user"/>
        </div>
        <button type="submit" class="btn btn-info custom-color">
            <span class="glyphicon glyphicon-search"></span> Search
        </button>

        <br/>
        <br/>
    </form>


    <c:if test="${not empty message}">
        <div class="alert alert-success">
                ${message}
        </div>
    </c:if>

    <!--Users List-->
    <form action="/user" method="post" id="userForm" role="form" >
        <input type="hidden" id="idUser" name="idUser">
        <input type="hidden" id="action" name="action">
    </form>
        <c:choose>
            <c:when test="${not empty userList}">
                <table  class="table table-hover">
                    <thead class="thead-inverse">
                    <tr>
                        <td>#</td>
                        <td>Name</td>
                        <td>E-mail</td>
                        <td>Role</td>
                        <td>Status</td>
                        <td>Creation Date</td>
                        <td>Group</td>
                        <td/>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="user" items="${userList}" varStatus="loop">
                        <c:set var="classSucess" value=""/>
                        <c:if test ="${idUser == user.id}">
                            <c:set var="classSucess" value="info"/>
                        </c:if>
                        <tr class="${classSucess}">
                            <td>
                                <a href="${pageContext.request.contextPath}/user?idUser=${user.id}&searchAction=searchById">${user.id}</a>
                            </td>
                            <td>${user.name}</td>
                            <td>${user.email}</td>
                            <td>${user.role}</td>
                            <td>${user.status}</td>
                            <td><fmt:formatDate value="${user.created}" pattern="dd-MM-yyyy" /></td>
                            <td>${user.group.name}</td>
                            <td><a href="#" id="remove"
                                   onclick="document.getElementById('idUser').value='${user.id}';
                                           document.getElementById('action').value='remove';
                                           document.getElementById('userForm').submit();">
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


    <form action ="/user" method="get" role="form">
        <br/>
        <input type="hidden"  name="searchAction" value="newUser">
        <button type="submit" class="btn btn-primary  btn-md">New user</button>
    </form>

</div>
</body>
</html>