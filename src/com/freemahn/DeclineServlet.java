package com.freemahn;

import com.cloudant.client.api.Database;
import com.google.gson.JsonArray;
import com.google.gson.stream.JsonReader;
import example.nosql.CloudantClientMgr;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by freeemahn on 12.07.15.
 */
public class DeclineServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean recordFound = true;
        String id = req.getParameter("id");

        Database db = CloudantClientMgr.getDB();

        //check if document exist
        HashMap<String, Object> obj = db.find(HashMap.class, id + "");

        if (obj == null)
            recordFound = false;
        else
            db.remove(obj);

        resp.sendRedirect("/api/dashboard");
       /* if (recordFound) {
            return Response.ok("OK").build();
        } else
            return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).build();*/
    }
}
