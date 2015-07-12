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
          <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
          <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
          <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</head>
<body>
<%ArrayList<User> users = (ArrayList<User>) request.getAttribute("users");%>

<div class="container">
  <h2>All CVs</h2>
  <table class="table table-bordered">
    <thead>
      <tr>
        <th>Email</th>
        <th>Test Result</th>
        <th>CV</th>
        <th>Video</th>
        <th>Actions</th>
      </tr>
    </thead>
    <tbody>
          <% for (User user : users) {%>
          <tr>
              <td><a href=<%out.print("mailto:"+user.getEmail());%> target="_top"><%out.print(user.getEmail());%></td>
              <td><%out.print(user.getResult());%></td>

              <td><a href=<% out.print(user.getAttachments().get(0).getUrl());%>><%
                  out.print(user.getAttachments().get(0).getKey());%></a></td>
              <td><a href=<% out.print(user.getAttachments().get(1).getUrl());%>><%
                  out.print(user.getAttachments().get(1).getKey());%></a></td>
              <td>
                <a href=<%out.print("/accept?id="+user.getId());%> class="btn btn-success" role="button">Accept</a>
                <a href=<%out.print("/decline?id="+user.getId());%> class="btn btn-danger" role="button">Decline</a>
              </td>
          </tr>
            <%} %>
      </tbody>
  </table>
</div>
</body>
</html>
