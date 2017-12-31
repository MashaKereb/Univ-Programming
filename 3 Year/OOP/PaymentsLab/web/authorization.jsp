<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Bootstrap Snippet: Login Form</title>
    <base href="/">
    <link rel='stylesheet prefetch' type="text/css"
          href='http://netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap.min.css'>
    <link rel="stylesheet" type="text/css" href="./css/auth.css">
</head>

<body>
<div class="wrapper">
    <form class="form-signin" action="/Main">
        <h2 class="form-signin-heading">Please login</h2>
        <input type="hidden" name="command" value="Authorization"/>
        <input type="text" class="form-control" name="email" placeholder="Email Address" required="" autofocus=""/>
        <input type="password" class="form-control" name="password" placeholder="Password" required=""/>
        <div class="msg-error">${message}</div>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Login</button>
    </form>
</div>
</body>
</html>


