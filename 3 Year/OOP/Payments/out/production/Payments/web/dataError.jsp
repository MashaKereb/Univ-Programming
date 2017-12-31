<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
Data input error! <br>
<c:out value="${requestScope.Error}"/><br>
<a href="Menu">Menu</a>
</body>
</html>
