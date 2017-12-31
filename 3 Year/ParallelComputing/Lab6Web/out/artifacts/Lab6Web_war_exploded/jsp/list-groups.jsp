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
    <div><div><h2>Groups</h2></div>  <div class="display-right"><a href="/user" ><h2>Users</h2></a></div></div>
    <!--Search Form -->
    <form action="/group" method="get" id="seachGroupForm" role="form">
        <input type="hidden" id="searchAction" name="searchAction" value="searchByName">
        <div class="form-group col-xs-5">
            <input type="text" name="groupName" value="${searchVal}" id="groupName" class="form-control" required="true" placeholder="Type the name or description of the group"/>
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

    <!--Groups List-->
    <form action="/group" method="post" id="groupForm" role="form" >
        <input type="hidden" id="idGroup" name="idGroup">
        <input type="hidden" id="action" name="action">
    </form>
        <c:choose>
            <c:when test="${not empty groupList}">
                <table  class="table table-hover">
                    <thead class="thead-inverse">
                    <tr>
                        <td>#</td>
                        <td>Name</td>
                        <td>Description</td>
                        <td>Creation date</td>
                        <td/>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="group" items="${groupList}" varStatus="loop">
                        <c:set var="classSucess" value=""/>
                        <c:if test ="${idGroup == group.id}">
                            <c:set var="classSucess" value="info"/>
                        </c:if>
                        <tr class="${classSucess}">
                            <td>
                                <a href="${pageContext.request.contextPath}/group?idGroup=${group.id}&searchAction=searchById">${group.id}</a>
                            </td>
                            <td>${group.name}</td>
                            <td>${group.description}</td>
                            <td><fmt:formatDate value="${group.created}" pattern="dd-MM-yyyy" /></td>

                            <td><a href="#" id="remove"
                                   onclick="document.getElementById('idGroup').value='${group.id}';
                                           document.getElementById('action').value='remove';
                                           document.getElementById('groupForm').submit();">
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
                    No group found matching your search criteria
                </div>
            </c:otherwise>
        </c:choose>


    <form action ="jsp/new-group.jsp">
        <br></br>
        <button type="submit" class="btn btn-primary  btn-md">New group</button>
    </form>

</div>
</body>
</html>