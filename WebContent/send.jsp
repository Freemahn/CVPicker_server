<%@page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sending file</title>
</head>
<body>
<h1>Send an email</h1>

<form action="api/upload" method="post" enctype="multipart/form-data">

    <input type="text" placeholder="name" name="name"/>
    <br/>

    <div>
        <input type="file" name="file">
    </div>

    <input type="submit"/>


</form>


</body>
</html>