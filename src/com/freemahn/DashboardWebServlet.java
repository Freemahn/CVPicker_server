package com.freemahn;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by freeemahn on 11.07.15.
 */

//for dashboard.jsp
public class DashboardWebServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Database db = CloudantClientMgr.getDB();
        List<User> list = new ArrayList<User>();
        String id = req.getParameter("id");
        List<HashMap> allUsers = db.view("_all_docs").query(HashMap.class);
        if (id == null)
            for (HashMap mapUser : allUsers) {
                try {
                    HashMap<String, Object> mapUser2 = db.find(HashMap.class, mapUser.get("id") + "");
                    LinkedTreeMap<String, Object> attachments = (LinkedTreeMap<String, Object>) mapUser2.get("_attachments");
                    User user = new User();
                    user.id = mapUser2.get("_id") + "";
                    user.email = mapUser2.get("email") + "";
                    user.result = Integer.parseInt(mapUser2.get("result") + "");
                    if (attachments != null && attachments.size() > 0) {
                        user.attachments = getAttachmentList(attachments, mapUser.get("id") + "");
                    }

                    list.add(user);
                } catch (Exception e) {
                    throw new ServletException("get From db exception" + e);
                }

            }
        else {

            HashMap<String, Object> obj = db.find(HashMap.class, id + "");
            LinkedTreeMap<String, Object> attachments = (LinkedTreeMap<String, Object>) obj.get("_attachments");
            User user = new User();
            user.id = obj.get("_id") + "";
            user.email = obj.get("email") + "";
            user.result = Integer.parseInt(obj.get("result") + "");

            if (attachments != null && attachments.size() > 0) {
                user.attachments = getAttachmentList(attachments, id + "");
            }

            list.add(user);
        }


        req.setAttribute("users", list);
        req.getRequestDispatcher("/dashboard.jsp").forward(req, resp);
    }


    private ArrayList<Attachment> getAttachmentList(LinkedTreeMap<String, Object> attachmentList, String
            docID) throws ServletException {

        ArrayList<Attachment> list = new ArrayList<Attachment>();
        try {
            String URLTemplate = "http://" + CloudantClientMgr.getUser() + ":" + CloudantClientMgr.getPassword() + "@" + CloudantClientMgr.getHost() + "/" + CloudantClientMgr.getDatabaseName() + "/";

            for (Object key : attachmentList.keySet()) {
                LinkedTreeMap<String, Object> attach = (LinkedTreeMap<String, Object>) attachmentList.get(key);
                Attachment attachment = new Attachment();

                //append the document id and attachment key to the URL
                attachment.url = URLTemplate + docID + "/" + key;
                //set the key of the attachment
                attachment.key = key + "";
                list.add(attachment);
            }
        } catch (Exception e) {
            throw new ServletException("GetAttachmentEx " + e);
        }
        return list;

    }
}
