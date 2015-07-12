package com.freemahn;

/**
 * Created by freeemahn on 12.07.15.
 */

import com.cloudant.client.api.Database;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import example.nosql.CloudantClientMgr;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by freeemahn on 11.07.15.
 */

//return json
public class DashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Database db = CloudantClientMgr.getDB();
        JsonArray jsonArray = new JsonArray();
        String id = req.getParameter("id");
        List<HashMap> allUsers = db.view("_all_docs").query(HashMap.class);
        if (id == null)
            for (HashMap user : allUsers) {
                HashMap<String, Object> obj = db.find(HashMap.class, user.get("id") + "");
                JsonObject jsonObject = new JsonObject();
                LinkedTreeMap<String, Object> attachments = (LinkedTreeMap<String, Object>) obj.get("_attachments");
                jsonObject.addProperty("id", obj.get("_id") + "");

                jsonObject.addProperty("result", obj.get("result") + "");
                jsonObject.addProperty("email", obj.get("email") + "");
                if (attachments != null && attachments.size() > 0) {
                    JsonArray attachmentList = null;
                    try {
                        attachmentList = getAttachmentList(attachments, user.get("id") + "");

                    } catch (Exception e) {
                        throw new ServletException(e);
                    }

                    jsonObject.add("attachments", attachmentList);

                }

                jsonArray.add(jsonObject);
            }
        else {
            HashMap<String, Object> obj = db.find(HashMap.class, id);
            JsonObject jsonObject = new JsonObject();
            LinkedTreeMap<String, Object> attachments = (LinkedTreeMap<String, Object>) obj.get("_attachments");
            jsonObject.addProperty("id", obj.get("_id") + "");
            jsonObject.addProperty("result", obj.get("result") + "");
            jsonObject.addProperty("email", obj.get("email") + "");
            if (attachments != null && attachments.size() > 0) {
                JsonArray attachmentList = null;
                try {
                    attachmentList = getAttachmentList(attachments, id);
                } catch (Exception e) {
                    throw new ServletException(e);
                }

                jsonObject.add("attachments", attachmentList);

            }

            jsonArray.add(jsonObject);
        }
        resp.getWriter().print(jsonArray.toString());


        //req.setAttribute("users", jsonArray);
       // req.getRequestDispatcher("/dashboard.jsp").forward(req, resp);
    }


    private JsonArray getAttachmentList(LinkedTreeMap<String, Object> attachmentList, String docID) throws Exception {

        JsonArray attachmentArray = new JsonArray();
        String URLTemplate = "http://" + CloudantClientMgr.getUser() + ":" + CloudantClientMgr.getPassword() + "@" + CloudantClientMgr.getHost() + "/" + CloudantClientMgr.getDatabaseName() + "/";

        for (Object key : attachmentList.keySet()) {
            LinkedTreeMap<String, Object> attach = (LinkedTreeMap<String, Object>) attachmentList.get(key);

            JsonObject attachedObject = new JsonObject();
            //set the content type of the attachment
            attachedObject.addProperty("content_type", attach.get("content_type").toString());
            //append the document id and attachment key to the URL
            attachedObject.addProperty("url", URLTemplate + docID + "/" + key);
            //set the key of the attachment
            attachedObject.addProperty("key", key + "");

            //add the attachment object to the array
            attachmentArray.add(attachedObject);
        }

        return attachmentArray;

    }
}
