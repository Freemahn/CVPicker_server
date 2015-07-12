<%@ page import="java.util.ArrayList" %>
<%@ page import="com.freemahn.User" %>
<%@ page import="com.freemahn.Attachment" %>
<%--
  Created by IntelliJ IDEA.
  User: freeemahn
  Date: 12.07.15
  Time: 5:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Dashboard</title>
</head>
<body>
<%ArrayList<User> users = (ArrayList<User>) request.getAttribute("users");%>


<table border="2">
    <caption>USERS</caption>
    <tr>
        <th>Id</th>
        <th>Test result</th>
        <th>CV</th>
        <th>Video</th>
    </tr>

    <% for (User user : users) {%>
    <tr>
        <td><%out.print(user.getId());%></td>
        <td><%out.print(user.getResult());%></td>

        <td><a href=<% out.print(user.getAttachments().get(0).getUrl());%>><%
            out.print(user.getAttachments().get(0).getKey());%></a></td>
        <td><a href=<% out.print(user.getAttachments().get(1).getUrl());%>><%
            out.print(user.getAttachments().get(1).getKey());%></a></td>
    </tr>

    <%} %>

</table>
</body>
</html>
