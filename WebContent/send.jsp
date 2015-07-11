<%@page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sending file</title>
</head>
<body>
<h1>Send an email</h1>

<form action="api/uploadCV" method="post" enctype="multipart/form-data">

    <div>
        <input type="file" name="file">
    </div>

    <input type="submit"/>


</form>


</body>
</html>